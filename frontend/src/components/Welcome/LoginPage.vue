<script setup>
import {User, Lock} from '@element-plus/icons-vue'
import {reactive} from "vue";
import {ElMessage} from "element-plus";
import {post} from "@/net/index.js";
import router from "@/router/index.js";

const form = reactive({
  username: '',
  password: '',
  remember: false
})

const login = () => {
  if (!form.password || !form.password) {
    ElMessage.warning('请填写用户名或者密码')
  } else {
    post('/api/auth/login', {username: form.username, password: form.password, remember: form.remember}, (message) => {
      ElMessage.success(message)
      router.push('/index')
    })
  }
}
</script>

<template>
  <div style="text-align: center;margin: 0 30px">
    <div style="margin-top: 150px">
      <div style="font-size: 25px;color: black">酥咔的小练手</div>
      <div style="font-size: 14px;color: grey">请先登陆</div>
    </div>
    <div style="margin-top: 100px">
      <el-input v-model="form.username" type="text" placeholder="用户名/邮箱" :prefix-icon="User"/>
    </div>
    <div style="margin-top: 20px">
      <el-input v-model="form.password" type="password" placeholder="密码" :prefix-icon="Lock"/>
    </div>
    <div style="margin-top: 10px">
      <el-row>
        <el-col :span="12" style="text-align: left;">
          <el-checkbox v-model="form.remember" label="自动登录" size="large"/>
        </el-col>
        <el-col :span="12" style="text-align: right;">
          <el-link>忘记密码</el-link>
        </el-col>
      </el-row>
    </div>
    <div style="margin-top: 30px">
      <el-button @click="login()" style="width: 200px" type="success" plain>登录</el-button>
    </div>
    <el-divider>
      <span style="color: grey; font-size: 12px">还没账号?</span>
    </el-divider>
    <div style="margin-top: 20px">
      <el-button style="width: 200px" type="warning" plain>注册</el-button>
    </div>
  </div>
</template>

<style scoped>

</style>