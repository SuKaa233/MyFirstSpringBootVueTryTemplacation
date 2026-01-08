<script setup>

</script>

<template>
  <div style="width: 100vw;height: 100vh; overflow: hidden; display: flex">
    <div style="flex: 1;">
      <el-image style="width: 100%;height: 100%" fit="cover"
                src="https://cdn.pixabay.com/photo/2023/03/20/20/35/sunset-7865844_1280.jpg"/>
    </div>
    <div class="welcometitle">
      <div style="font-size: 30px;font-weight: bold">
        本网站开发技术栈为"springboot + vue3"
      </div>
      <div style="margin-top: 20px">
        酥咔手敲制作
      </div>
    </div>
    <div style="width : 400px;background-color: white">
      <router-view/>
    </div>
  </div>
</template>

<style scoped>
.welcometitle{
  position: absolute;
  bottom: 30px ;
  left: 30px;
  color: white;
  text-shadow: 0 0 10px black;
}
</style>