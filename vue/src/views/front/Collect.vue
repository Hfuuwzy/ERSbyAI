<template>
    <div style="min-height: 1000px; background-color: #f6f6f8">
        <div style="width: 70%; margin:0 auto; text-align: center;font-size: 20px; font-weight: bold;padding: 50px 0">
            我收藏的岗位（{{ data.collectData.length }}）
        </div>
        <div style="width: 70%;margin: 30px auto">
            <el-row :gutter="10">
                <el-col :span="8" v-for="it in data.collectData" style="margin-bottom: 20px">
                    <div class="card"
                    >
                        <div style="display: flex; padding: 0 5px;cursor: pointer;"
                             @click="navTo('/front/positionDetail?id=' + it.positionId )">
                            <div style="flex: 1; text-align: left; font-size: 16px">{{ it.positionName }}</div>
                            <div style="width: 100px; text-align: right; color: rgb(254, 87, 74)">
                                {{ it.positionSalary }}
                            </div>
                        </div>
                        <div style="margin: 10px 0; padding: 0 5px; text-align: left">
                            <el-tag style="margin-right: 5px" type="info" v-for="tag in it.tagList">{{
                                    tag
                                }}
                            </el-tag>
                        </div>
                        <div style="display: flex; align-items: center; padding: 10px 5px">
                            <div style="width: 35px">
                                <img :src="it.employAvatar" alt=""
                                     style="width: 35px; height: 35px; border-radius: 5px; border: 1px solid #cccccc">
                            </div>
                            <div style="width: 80px;margin-left: 10px;">{{ it.employName }}</div>
                            <div style="flex: 1">{{ it.industryName }}</div>
                            <div style="width: 80px">{{ it.employStage }}</div>
                            <el-icon style="color:red;width: 25px;text-align: right;cursor: pointer"
                                     @click="cancelCollect(it.id)">
                                <Delete/>
                            </el-icon>
                        </div>
                    </div>
                </el-col>
            </el-row>
        </div>
    </div>
</template>

<script setup>
import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete} from "@element-plus/icons-vue";

const data = reactive({
    collectData: []
})
const loadCollect = () => {
    request.get('/collect/selectAll').then((res) => {
        if (res.code === '200') {
            data.collectData = res.data
        } else {
            ElMessage.error(res.msg)
        }
    })
}
const navTo = (url) => {
    location.href = url
}
const cancelCollect = (positionId) => {
    ElMessageBox.confirm("确定删除该收藏岗位吗？", '确定', {type: "warning"}).then((res) => {
        request.delete('/collect/delete/' + positionId).then((res) => {
            if (res.code === '200') {
                ElMessage.success("取消收藏成功")
                loadCollect()
            } else {
                ElMessage.error(res.msg)
            }
        })
    }).catch(err => {
        console.log(err)
    })
}
loadCollect()
</script>