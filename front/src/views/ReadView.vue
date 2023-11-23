<script setup lang="ts">
import {ref} from "vue";
import axios from 'axios';
import {defineProps, onMounted} from "vue";
import {useRouter} from "vue-router";

const post = ref({

});

const props = defineProps({
  postId:{
    type: [String, Number],
    required: true,
  }
})

onMounted(() => {

  axios.get(`/api/posts/${props.postId}`)
      .then((responce) => {
        console.log(responce.data)
        post.value = responce.data;
      })
      .catch((error) => {
        console.log(error)
      })
      .finally(function () {
        console.log("complete")
      });
})

const router = useRouter();

const moveToEdit = () => {
  router.push({name: "edit", params :{postId:props.postId}});
};

const deleteContent = ()=>{
  axios.delete(`/api/posts/${props.postId}`)
      .then((responce) => {
        router.replace({name: "home"});
      })
}


</script>
<template>
  <h2>{{ post.title }}</h2>
  <div>{{post.content }}</div>

  <div class="button">
    <el-button type="warning" @click="moveToEdit()">수정</el-button>
    <el-button type="danger" @click="deleteContent()">삭제</el-button>
  </div>

</template>
<style>
  .button{
    margin-top: 30px;
    text-align : right;
  }


</style>