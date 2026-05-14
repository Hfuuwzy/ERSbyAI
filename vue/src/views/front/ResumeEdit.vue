<template>
    <div class="resume-container resume-editor-container" style="min-height: 500px; width: 50%; margin: 20px auto">
        <div class="resume-editor-header" style="text-align: center; font-size: 20px">
            在线编辑您的简历
        </div>
        <div class="input-row" style="margin-top: 20px">
            <el-input class="resume-input" style="width: 50%; padding-right: 5px" v-model="data.resumeData.name"
                      placeholder="请输入简历名称"></el-input>
            <el-input class="resume-input" style="width: 50%; padding-left: 5px" v-model="data.resumeData.username"
                      placeholder="请输入姓名"></el-input>
        </div>
        <div class="input-row" style="margin-top: 10px">
            <el-select class="resume-select" v-model="data.resumeData.sex" placeholder="请选择性别"
                       style="width: 50%; padding-right: 5px">
                <el-option label="男" value="男"></el-option>
                <el-option label="女" value="女"></el-option>
            </el-select>
            <el-select class="resume-select" v-model="data.resumeData.salary" placeholder="请选择你的期望薪资"
                       style="width: 50%; padding-left: 5px">
                <el-option label="3k以下" value="3k以下"></el-option>
                <el-option label="3-5k" value="3-5k"></el-option>
                <el-option label="5-10k" value="5-10k"></el-option>
                <el-option label="10-20k" value="10-20k"></el-option>
                <el-option label="20-50k" value="20-50k"></el-option>
                <el-option label="50k以上" value="50k以上"></el-option>
            </el-select>
        </div>
        <div class="input-row" style="margin-top: 10px">
            <el-select class="resume-select" v-model="data.resumeData.education" placeholder="请选择你的学历"
                       style="width: 50%; padding-right: 5px">
                <el-option label="初中及以下" value="初中及以下"></el-option>
                <el-option label="中专/中技" value="中专/中技"></el-option>
                <el-option label="高中" value="高中"></el-option>
                <el-option label="大专" value="大专"></el-option>
                <el-option label="本科" value="本科"></el-option>
                <el-option label="硕士" value="硕士"></el-option>
                <el-option label="博士" value="博士"></el-option>
                <el-option label="博士后" value="博士后"></el-option>
            </el-select>
            <el-select class="resume-select" v-model="data.resumeData.experience" placeholder="请选择你的工作年限"
                       style="width: 50%; padding-left: 5px">
                <el-option label="在校生" value="在校生"></el-option>
                <el-option label="应届生" value="应届生"></el-option>
                <el-option label="1年以内" value="1年以内"></el-option>
                <el-option label="1到3年" value="1到3年"></el-option>
                <el-option label="3到5年" value="3到5年"></el-option>
                <el-option label="5到10年" value="5到10年"></el-option>
                <el-option label="10年以上" value="10年以上"></el-option>
            </el-select>
        </div>
        <div class="input-row" style="margin-top: 10px">
            <el-input class="resume-input" style="width: 50%; padding-right: 5px" v-model="data.resumeData.phone"
                      placeholder="请输入联系电话"></el-input>
            <el-input class="resume-input" style="width: 50%; padding-left: 5px" v-model="data.resumeData.email"
                      placeholder="请输入联系邮箱"></el-input>
        </div>
        <!--添加教育经历-->
        <div class="action-section" style="margin-top: 20px">
            <el-button class="action-button" type="success" @click="addEduExp">添加教育经历</el-button>
        </div>
        <div class="experience-section" style="margin-top: 10px; font-size: 16px"
             v-if="data.resumeData.eduExpList.length">
            <div class="section-title" style="font-weight: bold">教育经历：</div>
            <div class="experience-item" style="margin-top: 10px; margin-bottom: 20px"
                 v-for="item in data.resumeData.eduExpList">
                <div style="display: flex; align-items: center">
                    <div class="experience-header" style="flex: 1; color: #00bebd">{{ item.start }} ~
                        {{ item.end ? item.end : '至今' }}
                    </div>
                    <div class="experience-header" style="flex: 1; color: #00bebd; font-weight: bold">{{
                            item.school
                        }}
                    </div>
                    <div class="experience-header" style="flex: 1; color: #00bebd; font-weight: bold">{{
                            item.speciality
                        }}（{{
                            item.education
                        }}）
                    </div>
                    <div class="experience-details" style="display: flex; width: 30px">
                        <el-icon style="color: #269dde; cursor: pointer;" @click="editEduExp(item)">
                            <Edit/>
                        </el-icon>
                        <el-icon style="color: red; cursor: pointer" @click="delEduExp(item.id)">
                            <Delete/>
                        </el-icon>
                    </div>
                </div>
                <div style="margin-top: 10px">主修课程：{{ item.course }}</div>
            </div>
            <div></div>
        </div>
        <!--添加工作经历-->
        <div class="action-section" style="margin-top: 20px">
            <el-button class="action-button" type="success" @click="addWorkExp">添加工作经历</el-button>
        </div>
        <div class="experience-section" style="margin-top: 10px; font-size: 16px"
             v-if="data.resumeData.workExpList.length">
            <div class="section-title" style="font-weight: bold">工作经历：</div>
            <div class="experience-item" style="margin-top: 10px; margin-bottom: 20px"
                 v-for="item in data.resumeData.workExpList">
                <div style="display: flex; align-items: center">
                    <div style="flex: 1; color: #00bebd">{{ item.start }} ~ {{ item.end ? item.end : '至今' }}</div>
                    <div style="flex: 1; color: #00bebd; font-weight: bold">{{ item.employ }}</div>
                    <div style="flex: 1; color: #00bebd; font-weight: bold">{{ item.position }}（{{ item.type }}）</div>
                    <div class="experience-details" style="display: flex; width: 30px">
                        <el-icon style="color: #00bebd; cursor: pointer" @click="editWorkExp(item)">
                            <Edit/>
                        </el-icon>
                        <el-icon style="color: red; cursor: pointer" @click="delWorkExp(item.id)">
                            <Delete/>
                        </el-icon>
                    </div>
                </div>
                <div style="margin-top: 10px"><strong>{{ item.project }}</strong>：{{ item.content }}</div>
            </div>
            <div></div>
        </div>
        <!--添加项目经历-->
        <div class="action-section" style="margin-top: 20px">
            <el-button class="action-button" type="success" @click="addProExp">添加项目经验</el-button>
        </div>
        <div class="experience-section" style="margin-top: 10px; font-size: 16px"
             v-if="data.resumeData.proExpList.length">
            <div class="section-title" style="font-weight: bold">工作经历：</div>
            <div class="experience-item" style="margin-top: 10px; margin-bottom: 20px"
                 v-for="item in data.resumeData.proExpList">
                <div style="display: flex; align-items: center">
                    <div style="flex: 1; color: #00bebd">{{ item.start }} ~ {{ item.end ? item.end : '至今' }}</div>
                    <div style="flex: 1; color: #00bebd; font-weight: bold">{{ item.name }}</div>
                    <div style="display: flex; width: 30px">
                        <el-icon style="color: #00bebd; cursor: pointer" @click="editProExp(item)">
                            <Edit/>
                        </el-icon>
                        <el-icon style="color: red; cursor: pointer" @click="delProExp(item.id)">
                            <Delete/>
                        </el-icon>
                    </div>
                </div>
                <div style="margin-top: 10px">项目介绍：{{ item.content }}</div>
            </div>
        </div>

        <!--保存简历-->
        <div class="save-section" style="margin-top: 50px; text-align: center">
            <el-button class="save-button" type="info" style="padding: 20px 30px" @click="saveResume">保存简历
            </el-button>
        </div>
        <!--弹出窗口-->
        <el-dialog class="resume-dialog" title="填写教育经历" v-model="data.eduFormVisible" width="40%"
                   destroy-on-close>
            <el-form class="resume-form" ref="form" :model="data.eduForm" label-width="70px" style="padding: 20px">
                <el-form-item prop="school" label="学校名称">
                    <el-input v-model="data.eduForm.school" placeholder="请输入学校名称"></el-input>
                </el-form-item>
                <el-form-item prop="speciality" label="专业名称">
                    <el-input v-model="data.eduForm.speciality" placeholder="请输入专业名称"></el-input>
                </el-form-item>
                <el-form-item prop="education" label="选择学历">
                    <el-select v-model="data.eduForm.education" placeholder="请选择你的学历"
                               style="width: 50%; padding-right: 5px; width: 100%">
                        <el-option label="初中及以下" value="初中及以下"></el-option>
                        <el-option label="中专/中技" value="中专/中技"></el-option>
                        <el-option label="高中" value="高中"></el-option>
                        <el-option label="大专" value="大专"></el-option>
                        <el-option label="本科" value="本科"></el-option>
                        <el-option label="硕士" value="硕士"></el-option>
                        <el-option label="博士" value="博士"></el-option>
                        <el-option label="博士后" value="博士后"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="start" label="入学时间">
                    <el-date-picker style="width: 100%"
                                    v-model="data.eduForm.start"
                                    type="date"
                                    placeholder="请选择日期"
                                    value-format="YYYY-MM-DD"
                    />
                </el-form-item>
                <el-form-item prop="end" label="毕业时间">
                    <el-date-picker style="width: 100%"
                                    v-model="data.eduForm.end"
                                    type="date"
                                    placeholder="请选择日期"
                                    value-format="YYYY-MM-DD"
                    />
                </el-form-item>
                <el-form-item prop="course" label="主修课程">
                    <el-input type="textarea" :rows="4" v-model="data.eduForm.course"
                              placeholder="请输入主修课程"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.eduFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveEduExp">确 定</el-button>
        </span>
            </template>
        </el-dialog>
        <el-dialog class="resume-dialog" title="填写工作经历" v-model="data.workFormVisible" width="40%"
                   destroy-on-close>
            <el-form class="resume-form" ref="form" :model="data.workForm" label-width="70px" style="padding: 20px">
                <el-form-item prop="employ" label="公司名称">
                    <el-input v-model="data.workForm.employ" placeholder="请输入公司名称"></el-input>
                </el-form-item>
                <el-form-item prop="project" label="项目名称">
                    <el-input v-model="data.workForm.project" placeholder="请输入项目名称"></el-input>
                </el-form-item>
                <el-form-item prop="position" label="职位名称">
                    <el-input v-model="data.workForm.position" placeholder="请输入职位名称"></el-input>
                </el-form-item>
                <el-form-item prop="type" label="职位类型">
                    <el-select v-model="data.workForm.type" placeholder="请选择职位类型"
                               style="width: 50%; padding-right: 5px; width: 100%">
                        <el-option label="全职" value="全职"></el-option>
                        <el-option label="实习" value="实习"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="start" label="入职时间">
                    <el-date-picker style="width: 100%"
                                    v-model="data.workForm.start"
                                    type="date"
                                    placeholder="请选择日期"
                                    value-format="YYYY-MM-DD"
                    />
                </el-form-item>
                <el-form-item prop="end" label="离职时间">
                    <el-date-picker style="width: 100%"
                                    v-model="data.workForm.end"
                                    type="date"
                                    placeholder="请选择日期"
                                    value-format="YYYY-MM-DD"
                    />
                </el-form-item>
                <el-form-item prop="content" label="项目介绍">
                    <el-input type="textarea" :rows="4" v-model="data.workForm.content"
                              placeholder="请输入项目介绍"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.workFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveWorkExp">确 定</el-button>
        </span>
            </template>
        </el-dialog>
        <el-dialog class="resume-dialog" title="填写工项目经验" v-model="data.proFormVisible" width="40%"
                   destroy-on-close>
            <el-form class="resume-form" ref="form" :model="data.proForm" label-width="70px" style="padding: 20px">
                <el-form-item prop="name" label="项目名称">
                    <el-input v-model="data.proForm.name" placeholder="请输入项目名称"></el-input>
                </el-form-item>
                <el-form-item prop="start" label="开始时间">
                    <el-date-picker style="width: 100%"
                                    v-model="data.proForm.start"
                                    type="date"
                                    placeholder="请选择日期"
                                    value-format="YYYY-MM-DD"
                    />
                </el-form-item>
                <el-form-item prop="end" label="结束时间">
                    <el-date-picker style="width: 100%"
                                    v-model="data.proForm.end"
                                    type="date"
                                    placeholder="请选择日期"
                                    value-format="YYYY-MM-DD"
                    />
                </el-form-item>
                <el-form-item prop="content" label="项目介绍">
                    <el-input type="textarea" :rows="4" v-model="data.proForm.content"
                              placeholder="请输入项目介绍"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.proFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveProExp">确 定</el-button>
        </span>
            </template>
        </el-dialog>
    </div>
</template>

<script setup>
import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Edit, Delete} from "@element-plus/icons-vue";
import Router from "@/router/index.js";

const data = reactive({
    resumeId: Router.currentRoute.value.query.id,
    user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    resumeData: {
        eduExpList: [],
        workExpList: [],
        proExpList: []
    },
    eduForm: {},
    workForm: {},
    proForm: {},
    eduFormVisible: false,
    workFormVisible: false,
    proFormVisible: false,

})

const loadResume = () => {
    data.resumeId = Router.currentRoute.value.query.id
    if (data.resumeId) {
        request.get('resume/selectById/' + data.resumeId).then((res) => {
            if (res.code === '200') {
                data.resumeData = res.data
            } else {
                ElMessage.error(res.msg)
            }
        })
    }
}


const saveResume = () => {
    if (data.resumeData.id) {
        // 更新
        request.put('/resume/update', data.resumeData).then((res) => {
            if (res.code === '200') {
                ElMessage.success("保存成功")
            } else {
                ElMessage.error(res.msg)
            }
        })
    } else {
        // 新增
        data.resumeData.userId = data.user.id
        request.post('/resume/add', data.resumeData).then(res => {
            if (res.code === '200') {
                ElMessage.success('保存成功')
                setTimeout(() => {
                    location.href = '/front/resume'
                }, 500)
            } else {
                ElMessage.error(res.msg)
            }
        })
    }
}

const addEduExp = () => {
    data.eduForm = {
        id: new Date().getTime() + Math.random().toString(36).substr(2) // 给个id，方便编辑
    }
    data.eduFormVisible = true
}
const addWorkExp = () => {
    data.workForm = {
        id: new Date().getTime() + Math.random().toString(36).substr(2) // 给个id，方便编辑
    }
    data.workFormVisible = true
}
const addProExp = () => {
    data.proForm = {
        id: new Date().getTime() + Math.random().toString(36).substr(2) // 给个id，方便编辑
    }
    data.proFormVisible = true
}
const saveEduExp = () => {
    let form = data.resumeData.eduExpList.filter((v => v.id === data.eduForm.id))
    if (form && form.length) {
        // 此时是更新
        data.resumeData.eduExpList.forEach(item => {
            if (item.id === data.eduForm.id) {
                item.school = data.eduForm.school
                item.speciality = data.eduForm.speciality
                item.start = data.eduForm.start
                item.end = data.eduForm.end
                item.course = data.eduForm.course
            }
        })
    } else {
        // console.log('此时是新增')
        data.resumeData.eduExpList.push(data.eduForm)
    }
    data.eduFormVisible = false
}
const saveWorkExp = () => {
    let form = data.resumeData.workExpList.filter((v => v.id === data.workForm.id))
    if (form && form.length) {
        // 此时是更新
        data.resumeData.workExpList.forEach(item => {
            if (item.id === data.workForm.id) {
                item.employ = data.workForm.employ
                item.project = data.workForm.project
                item.position = data.workForm.position
                item.type = data.workForm.type
                item.start = data.workForm.start
                item.end = data.workForm.end
                item.content = data.workForm.content
            }
        })
    } else {
        // console.log('此时是新增')
        data.resumeData.workExpList.push(data.workForm)
    }
    data.workFormVisible = false
}
const saveProExp = () => {
    let form = data.resumeData.proExpList.filter(v => v.id === data.proForm.id)
    if (form && form.length) {
        // console.log('此时是更新')
        data.resumeData.proExpList.forEach(item => {
            if (item.id === data.proForm.id) {
                item.name = data.proForm.name
                item.start = data.proForm.start
                item.end = data.proForm.end
                item.content = data.proForm.content
            }
        })
    } else {
        // console.log('此时是新增')
        data.resumeData.proExpList.push(data.proForm)
    }
    data.proFormVisible = false
}

const editEduExp = (item) => {
    data.eduForm = JSON.parse(JSON.stringify(item))
    data.eduFormVisible = true
}
const editWorkExp = (item) => {
    data.workForm = JSON.parse(JSON.stringify(item))
    data.workFormVisible = true
}
const editProExp = (item) => {
    data.proForm = JSON.parse(JSON.stringify(item))
    data.proFormVisible = true
}
const delEduExp = (id) => {
    ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', {type: 'warning'}).then(res => {
        // 删除教育经历
        data.resumeData.eduExpList = data.resumeData.eduExpList.filter(v => v.id !== id)
    }).catch(err => {
        console.error(err)
    })
}
const delWorkExp = (id) => {
    ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', {type: 'warning'}).then(res => {
        // 删除工作经历
        data.resumeData.workExpList = data.resumeData.workExpList.filter(v => v.id !== id)
    }).catch(err => {
        console.error(err)
    })
}
const delProExp = (id) => {
    ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', {type: 'warning'}).then(res => {
        // 删除教育经历
        data.resumeData.proExpList = data.resumeData.proExpList.filter(v => v.id !== id)
    }).catch(err => {
        console.error(err)
    })
}

loadResume()


</script>

<style scoped>
.resume-container {
    min-height: 100vh;
    display: flex;
    flex-direction: column;
}

/* 主容器样式 */
.resume-editor-container {
    background: #ffffff;
    border-radius: 12px;
    box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);
    padding: 40px;
    transition: all 0.3s ease;
}

/* 标题样式 */
.resume-editor-header {
    color: #2c3e50;
    font-size: 28px !important;
    font-weight: 600;
    letter-spacing: 1px;
    padding-bottom: 25px;
    border-bottom: 2px solid #f0f2f5;
    margin-bottom: 30px;
}

/* 输入行布局 */
.input-row {
    display: flex;
    gap: 15px;
    margin-bottom: 20px !important;
}

/* 输入框统一样式 */
.resume-input .el-input__wrapper {
    border-radius: 8px !important;
    background: #f8f9fa !important;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05) !important;
    transition: all 0.3s ease;
}

.resume-input .el-input__wrapper:hover {
    box-shadow: 0 4px 8px rgba(67, 143, 255, 0.15) !important;
}

/* 选择器组样式 */
.selector-group {
    display: flex;
    gap: 15px;
    margin-bottom: 20px !important;
}

.resume-select .el-select__wrapper {
    border-radius: 8px !important;
    background: #f8f9fa !important;
}

/* 操作按钮样式 */
.action-button {
    width: 100% !important;
    border-radius: 8px !important;
    background: linear-gradient(135deg, #00c6ff, #0072ff) !important;
    border: none !important;
    color: white !important;
    padding: 18px 0 !important;
    transition: transform 0.2s ease;
}

.action-button:hover {
    transform: translateY(-2px);
    box-shadow: 0 5px 15px rgba(0, 198, 255, 0.3) !important;
}

/* 经历区块样式 */
.experience-section {
    background: #f9fbfd;
    border-radius: 10px;
    padding: 20px;
    margin-bottom: 25px;
    border: 1px solid #e8ecef;
}

.section-title {
    color: #2c3e50;
    font-size: 18px;
    font-weight: 600;
    margin-bottom: 15px;
    padding-left: 10px;
    border-left: 4px solid #0072ff;
}

.experience-item {
    background: white;
    border-radius: 8px;
    padding: 18px;
    margin-bottom: 15px;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.04);
    transition: all 0.3s ease;
}

.experience-item:hover {
    transform: translateX(5px);
    box-shadow: 0 5px 15px rgba(0, 114, 255, 0.1);
}

/* 保存按钮样式 */
.save-button {
    border-radius: 8px !important;
    background: linear-gradient(135deg, #30cfd0, #085967) !important;
    border: none !important;
    transition: all 0.3s ease !important;
    font-size: 16px !important;
}

.save-button:hover {
    transform: scale(1.05);
    box-shadow: 0 8px 20px rgba(51, 8, 103, 0.2) !important;
}

/* 对话框样式 */
.resume-dialog .el-dialog {
    border-radius: 12px !important;
    overflow: hidden;
}

.resume-form .el-form-item__label {
    color: #5a6c7e !important;
    font-weight: 500 !important;
}

/* 响应式调整 */
@media (max-width: 768px) {
    .resume-editor-container {
        width: 90% !important;
        padding: 25px;
    }

    .input-row,
    .selector-group {
        flex-direction: column;
    }

    .resume-input,
    .resume-select {
        width: 100% !important;
        padding: 0 !important;
    }
}

/* 动画效果 */
@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translateY(10px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.experience-item {
    animation: fadeIn 0.4s ease forwards;
}

:root {
    --primary-color: #0072ff;
    --secondary-color: #00c6ff;
    --background-light: #f9fbfd;
    --text-dark: #2c3e50;
    --border-radius: 8px;
}
</style>