<script setup>

import {Lock, Message, User} from "@element-plus/icons-vue";
import router from "@/router/index.js";

import {reactive, ref} from "vue";
import {ElMessage} from "element-plus";
import {post} from "@/net/index.js";

const form = reactive({
  username: "",
  password: "",
  passwordconfirm: "",
  email: "",
  code: ""
})

const validatePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== form.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const validateUsername = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入用户名'))
  } else if (!/^[A-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)) {
    callback(new Error('用户名不能有特殊字符'))
  } else {
    callback()
  }
}


const rules = {
  username: [
    {validator: validateUsername, trigger: ['blur', 'change']},
    {min: 3, max: 25, message: '长度应在3到25之间', trigger: ['blur', 'change']}
  ], password: [
    {required: true, message: '请输入密码', trigger: ['blur']},
    {min: 6, max: 15, message: '长度应在6到15之间', trigger: ['blur', 'change']},
  ], passwordconfirm: [
    {validator: validatePassword, trigger: ['blur', 'change']},
  ], email: [
    {required: true, message: '请输入邮件地址', trigger: ['blur']},
    {type: 'email', message: '请输入正确的邮件地址', trigger: ['blur', 'change']},
  ], code: [
    {required: true, message: '请输入验证码', trigger: ['blur']},
  ]
}
const isEmailValid = ref(false)
const onValidate = (prop, isValid) => {
  if (prop === 'email') {
    isEmailValid.value = isValid
  }
}
const formRef = ref()
const register = () => {
  formRef.value.validate((isValid) => {
  if (isValid) {
    post('api/auth/register',{
      email: form.email,
      password: form.password,
      username: form.username,
      code: form.code,
    },(message)=>{
      ElMessage.success(message)
      router.push('/')
    })
  }else {
    ElMessage.warning('请完整填写上述注册表单内容')
  }
  })
}

const validateEmail = () => {
  post('/api/auth/valid-email'
      ,{email: form.email}
      ,(message)=>{ElMessage.success(message)
        coldTime.value=60
        setInterval(() => coldTime.value--, 1000)
  })
}

const coldTime = ref(0)
</script>

<template>
  <div style="text-align: center;margin: 0 30px">
    <div style="margin-top: 100px">
      <div style="font-size: 25px;color: black">注册</div>
      <div style="font-size: 14px;color: grey">欢迎注册</div>
    </div>
    <div style="margin-top: 50px">
      <el-form :model="form" :rules="rules" @validate="onValidate" ref="formRef">
        <el-form-item prop="username">
          <el-input :maxlength="25" type="text" v-model="form.username" placeholder="用户名" :prefix-icon="User">

          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input :maxlength="15" type="password" v-model="form.password" placeholder="密码"
                    :prefix-icon="Lock">

          </el-input>
        </el-form-item>
        <el-form-item prop="passwordconfirm">
          <el-input :maxlength="15" type="password" v-model="form.passwordconfirm" placeholder="重复密码"
                    :prefix-icon="Lock">

          </el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input type="text" v-model="form.email" placeholder="电子邮件地址"
                    :prefix-icon="Message">

          </el-input>
        </el-form-item>
        <el-form-item prop="code">
          <el-row :gutter="10" style="width: 100%">
            <el-col :span="17">
              <el-input :maxlength="6" v-model="form.code" type="text" placeholder="验证码">
              </el-input>
            </el-col>
            <el-col :span="4">
              <el-button @click="validateEmail" type="success"
                         :disabled="!isEmailValid || coldTime > 0">
                {{ coldTime > 0 ? '请稍后' + coldTime + '秒' : '获取验证码'}}
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>
    </div>
    <div style="margin-top: 80px">
      <el-button  style="width: 270px" type="warning" @click="register" plain>
        确认注册!
      </el-button>
    </div>
    <div style="margin-top: 20px;font-size: 15px; color: grey">
      点错了?
      <el-link @click="router.push('/')" type="primary" plain style="translate: 0 -2px">
        我要回去!
      </el-link>
    </div>
  </div>
</template>

<style scoped>

</style>