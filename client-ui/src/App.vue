<template>
  <div id="app">
    <img alt="Vue logo" src="./assets/logo.png">
    <!-- <HelloWorld msg="Welcome to Your Vue.js App"/> -->
    <span class="title-text">Blog admin page</span>
    <div class="row justify-content-center mt-4">
      <input v-model="inputField" v-on=keyup.enter="createBlog" class="mr-1" placeholder="Create Blog Title" >
    </div>
  </div>
</template>

<script>
import HelloWorld from './components/HelloWorld.vue'
import { CreateBlogRequest } from "./blog_pb";
import { blogServiceClient } from "./blog_grpc_web_pb";


export default {
  name: 'app',
  components: {
    HelloWorld
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
