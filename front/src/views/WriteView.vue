<script setup lang="ts">
import {ref} from "vue";
import axios from 'axios';
import {useRouter} from "vue-router";
//
// const axios = require('axios').default;

const title=ref("")
const content=ref("")
const router = useRouter()

const write= function(){
  //sconsole.log(title.value, content.value)
  axios.post('/api/posts', {
    title: title.value,
    content: content.value
  })
  .then(function (responce){
    //push, replace 차이 찾아보기.!!
    router.replace({name:"home"});
  })
    .catch(function (error) {
      console.log(error);
    });
}
</script>

<template>
  <div>
    <el-input v-model="title"  placeholder="제목을 입력해 주세요."/>
  </div>

  <div>
    <el-input v-model="content"  type="textarea"  placeholder="내용을 입력해 주세요." rows="15"></el-input>
  </div>

  <div class="button">
    <el-button type="primary" @click="write">글 작성 완료</el-button>
  </div>

</template>

<style>

.button{
  margin-top: 30px;
  text-align : right;
}
</style>