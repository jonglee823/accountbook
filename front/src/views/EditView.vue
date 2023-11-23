<script setup lang="ts">
import {ref} from "vue";
import axios from 'axios';
import {defineProps, onMounted} from "vue";
import {useRouter} from "vue-router";

const post = ref({});

const router = useRouter()

const props = defineProps({
  postId:{
    type: [String, Number],
    required: true,
  },
});

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

const edit = ()=>{
  axios.patch(`/api/posts/${props.postId}`, post.value)
      .then((responce) => {
        router.replace({name:"home"})
      })
      .catch((error) => {
        console.log(error)
      })
      .finally(function () {
        console.log("complete")
      });
}

</script>
<template>
  <div>
    <el-input v-model="post.title"></el-input>
  </div>

  <div>
    <el-input v-model="post.content" type="textarea"></el-input>
  </div>

  <div class="button">
    <el-button type="warning" @click="edit()">수정 완료</el-button>
  </div>
</template>

<style>
.button{
  margin-top: 30px;
  text-align : right;
}
</style>
