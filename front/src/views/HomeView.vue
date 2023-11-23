<script setup lang="ts">

import {ref} from "vue";
import axios from 'axios';
import {useRouter} from "vue-router";

const posts= ref([]);
const router = useRouter();

axios.get('/api/posts?page=1&size=10')
    .then((responce) => {
      console.log(responce.data)
      responce.data.forEach((r: any) =>{
        posts.value.push(r);
      });
    })
    .catch((error) => {
      console.log(error)
    });

</script>


<template>
  <ul>
    <li v-for="post in posts" :key="post.id">
      <div class="title">
        <router-link  :to="{name: 'read', params:{postId:post.id}}">{{post.title}}</router-link>
      </div>

      <div class="content">
        {{post.content}}
      </div>
    </li>
  </ul>

  <div class="example-pagination-block">
    <el-pagination background layout="prev, pager, next" :total="1000" />
  </div>
</template>

<style scoped lang="scss">
ul {
  list-style: none;
  padding: 0;

  li {
    margin-bottom: 2rem;

    .title {
      a {
        font-size: 1.1rem;
        color: #383838;
        text-decoration: none;
      }

      &:hover {
        text-decoration: underline;
      }
    }

    .content {
      font-size: 0.85rem;
      margin-top: 8px;
      line-height: 1.4;
      color: #7e7e7e;
    }

    &:last-child {
      margin-bottom: 0;
    }

    .sub {
      margin-top: 8px;
      font-size: 0.78rem;

      .regDate {
        margin-left: 10px;
        color: #6b6b6b;
      }
    }
  }
}

.example-pagination-block + .example-pagination-block {
  margin-top: 10px;
}
.example-pagination-block .example-demonstration {
  margin-bottom: 16px;
}
</style>