<script setup>
import {User,Lock} from '@element-plus/icons-vue'
</script>

<template>
  <div style="text-align: center;margin: 0 30px">
    <div style="margin-top: 150px">
      <div style="font-size: 25px;color: black">酥咔的小练手</div>
      <div style="font-size: 14px;color: grey">请先登陆</div>
    </div>
    <div style="margin-top: 100px">
      <el-input type="text" placeholder="用户名/邮箱" :prefix-icon="User"/>
    </div>
    <div style="margin-top: 20px">
      <el-input type="password" placeholder="密码" :prefix-icon="Lock"/>
    </div>
    <div style="margin-top: 10px">
      <el-row>
        <el-col :span="12" style="text-align: left;">
          <el-checkbox v-model="checked1" label="自动登录" size="large"/>
        </el-col>
        <el-col :span="12" style="text-align: right;">
          <el-link>忘记密码</el-link>
        </el-col>
      </el-row>
    </div>
    <div style="margin-top: 30px">
      <el-button style="width: 200px" type="success" plain>登录</el-button>
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