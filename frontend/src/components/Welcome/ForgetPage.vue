<script setup>
import {reactive, ref} from "vue";
import {Lock, Message} from "@element-plus/icons-vue";
import {post} from "@/net/index.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const form = reactive({
  email: '',
  code: '',
  password: '',
  passwordconfirm: ""
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

const rules = {
  email: [
    {required: true, message: '请输入邮件地址', trigger: ['blur']},
    {type: 'email', message: '请输入正确的邮件地址', trigger: ['blur', 'change']},
  ], code: [
    {required: true, message: '请输入验证码', trigger: ['blur']},
  ],password: [
    {required: true, message: '请输入密码', trigger: ['blur']},
    {min: 6, max: 15, message: '长度应在6到15之间', trigger: ['blur', 'change']},
  ], passwordconfirm: [
    {validator: validatePassword, trigger: ['blur', 'change']},
  ]
}

const isEmailValid = ref(false)
const formRef = ref()
const coldTime = ref(0)
const validateEmail = () => {
  post('/api/auth/valid-reset-email'
      ,{email: form.email}
      ,(message)=>{ElMessage.success(message)
        coldTime.value=60
        setInterval(() => coldTime.value--, 1000)
      })
}
const onValidate = (prop, isValid) => {
  if (prop === 'email') {
    isEmailValid.value = isValid
  }
}
const active = ref(0)

const startReset = () => {
  formRef.value.validate((isValid) => {
    if (isValid) {
      post('api/auth/start-reset',{email: form.email,code: form.code},
          ()=>{active.value++})
    }else {
      ElMessage.warning('请填写电子邮件地址和验证码')
    }
  })
}

const doReset = () => {
  formRef.value.validate((isValid) => {
    if (isValid) {
      post('api/auth/do-reset',{password: form.password},
          (message)=>{ElMessage.success(message)
            router.push('/')})
    }else {
      ElMessage.warning('请填写新的密码')
    }
  })
}
</script>

<template>
    <transition name="el-fade-in-linear" mode="out-in">
      <div style="text-align: center;margin:0 30px;height: 100%" v-if="active === 0 ">
        <div style="margin-top: 100px">
          <div style="font-size: 25px;color: black">重置密码</div>
          <div style="font-size: 14px;color: grey">请输入要重置密码的电子邮件地址</div>
          <div style="margin: 30px 20px;">
            <el-steps :active="active" finish-status="success" align-center>
              <el-step title="验证电子邮件"/>
              <el-step title="重新设定密码"/>
              <el-step title="确定重置"/>
            </el-steps>
          </div>
          <div style="margin-top: 50px">
            <el-form :model="form" :rules="rules" @validate="onValidate" ref="formRef">
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
                      {{ coldTime > 0 ? '请稍后' + coldTime + '秒' : '获取验证码' }}
                    </el-button>
                  </el-col>
                </el-row>
              </el-form-item>
            </el-form>
          </div>
        </div>
        <div style="text-align: center;margin: 60px">
          <el-button @click = "startReset()" style="width: 270px;" type="success" plain>下一步</el-button>
        </div>
      </div>
    </transition>
    <transition name="el-fade-in-linear" mode="out-in">
      <div style="text-align: center;margin:0 30px;height: 100%" v-if="active === 1 ">
        <div style="margin-top: 100px">
          <div style="font-size: 25px;color: black">重置密码</div>
          <div style="font-size: 14px;color: grey">请填写新密码</div>
          <div style="margin: 30px 20px;">
            <el-steps :active="active" finish-status="success" align-center>
              <el-step title="验证电子邮件"/>
              <el-step title="重新设定密码"/>
              <el-step title="确定重置"/>
            </el-steps>
          </div>
        </div>
        <div style="margin-top: 50px">
          <el-form :model="form" :rules="rules" @validate="onValidate" ref="formRef">
            <el-form-item prop="password">
              <el-input :maxlength="15" type="password" v-model="form.password" placeholder="新密码"
                        :prefix-icon="Lock">
              </el-input>
            </el-form-item>
            <el-form-item prop="passwordconfirm">
              <el-input :maxlength="15" type="password" v-model="form.passwordconfirm" placeholder="重复新密码"
                        :prefix-icon="Lock">
              </el-input>
            </el-form-item>
          </el-form>
          <div style="text-align: center;margin: 60px">
            <el-button @click = "doReset()" style="width: 270px;" type="danger" plain>确定重置</el-button>
          </div>
        </div>
      </div>
    </transition>
</template>

<style scoped>

</style>