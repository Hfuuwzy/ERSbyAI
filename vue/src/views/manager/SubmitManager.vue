<template>
  <div>
    <!-- Search Card -->
    <div class="card" style="margin-bottom: 5px">
      <el-input 
        v-model="data.positionName" 
        prefix-icon="Search" 
        style="width: 240px; margin-right: 10px"
        placeholder="请输入岗位名称查询"
      />
      <el-button type="info" plain @click="load">查询</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button>
    </div>

    <!-- Batch Actions Card - Only for EMPLOY -->
    <div class="card" style="margin-bottom: 5px" v-if="data.user.role === 'EMPLOY'">
      <el-tag type="info" style="margin-right: 10px">已选择 {{ data.ids.length }} 项</el-tag>
      <el-button 
        type="danger" 
        :disabled="!data.ids.length" 
        @click="batchUpdate('不适合')"
      >批量不适合</el-button>
      <el-button 
        type="primary" 
        :disabled="!data.ids.length" 
        @click="batchUpdate('面试中')"
      >批量面试中</el-button>
      <el-button 
        type="success" 
        :disabled="!data.ids.length" 
        @click="batchUpdate('通过')"
      >批量通过</el-button>
      <el-button 
        type="warning" 
        :disabled="!data.ids.length" 
        @click="batchUpdate('不通过')"
      >批量不通过</el-button>
    </div>

    <!-- Table -->
    <div class="card" style="margin-bottom: 5px">
      <el-table 
        stripe 
        :data="data.tableData"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" v-if="data.user.role === 'EMPLOY'" />
        <el-table-column prop="employName" label="企业名称"/>
        <el-table-column prop="positionName" label="岗位名称" />
        <el-table-column prop="userName" label="投递用户"/>
        <el-table-column prop="resumeId" label="投递简历">
          <template #default="scope">
            <a :href="'/resumeView?id=' + scope.row.resumeId" target="_blank">简历预览</a>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="投递时间"/>
        <el-table-column prop="status" label="投递状态">
          <template #default="scope">
            <el-tag v-if="scope.row.status === '不适合'" type="danger">{{ scope.row.status }}</el-tag>
            <el-tag v-else-if="scope.row.status === '面试中'" type="primary">{{ scope.row.status }}</el-tag>
            <el-tag v-else-if="scope.row.status === '通过'" type="success">{{ scope.row.status }}</el-tag>
            <el-tag v-else-if="scope.row.status === '不通过'" type="warning">{{ scope.row.status }}</el-tag>
            <el-tag v-else type="info">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="400" fixed="right" v-if="data.user.role === 'EMPLOY'">
          <template #default="scope">
            <el-button @click="updateStatus(scope.row, '不适合')" type="danger" size="small">不适合</el-button>
            <el-button @click="updateStatus(scope.row, '面试中')" type="primary" size="small">面试中</el-button>
            <el-button @click="updateStatus(scope.row, '通过')" type="success" size="small">通过</el-button>
            <el-button @click="updateStatus(scope.row, '不通过')" type="warning" size="small">不通过</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- Pagination -->
    <div class="card" v-if="data.total">
      <el-pagination 
        @current-change="load" 
        background 
        layout="prev, pager, next" 
        :page-size="data.pageSize"
        v-model:current-page="data.pageNum" 
        :total="data.total"
      />
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import { ElMessage, ElMessageBox } from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  positionName: null,
  ids: [],
});

const load = () => {
  request.get('/submit/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      positionName: data.positionName
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || [];
      data.total = res.data?.total;
    }
  });
};

const handleSelectionChange = (rows) => {
  data.ids = rows.map(row => row.id);
};

const batchUpdate = (status) => {
  if (!data.ids.length) {
    ElMessage.warning('请选择要操作的简历');
    return;
  }
  
  ElMessageBox.confirm(
    `确定要批量标记为"${status}"吗？`,
    '批量操作确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    request.post('/submit/batchUpdate', { 
      ids: data.ids, 
      status 
    }).then(res => {
      if (res.code === '200') {
        ElMessage.success('批量操作成功');
        data.ids = [];
        load();
      } else {
        ElMessage.error(res.msg);
      }
    });
  }).catch(() => {});
};

const updateStatus = (row, status) => {
  let form = JSON.parse(JSON.stringify(row));
  form.status = status;
  request.put('/submit/update', form).then((res) => {
    if (res.code === '200') {
      ElMessage.success('操作成功');
      load();
    } else {
      ElMessage.error(res.msg);
    }
  });
};

const reset = () => {
  data.positionName = null;
  data.pageNum = 1;
  data.ids = [];
  load();
};

load();
</script>

<style scoped>
</style>
