<template>
  <el-container class="article_list">
    <!--感觉main的作用就是分的更开了,格式好看了-->
    <el-main class="main">
      <!--点击了总的有个响应的地方吧,响应呢?-->
      <!--答：使用了组件，blog_table和blog_cfg-->
      <el-tabs v-model="activeName" @tab-click="handleClick" type="card">
        <!--根据绑定的方法，点击后自动把label传给activateName-->
        <el-tab-pane label="全部文章" name="all">
          <!--使用组件的同时传递参数-->
          <blog_table state="-1" :showEdit="false" :showDelete="false" :showRestore="false" :activeName="activeName"></blog_table>
        </el-tab-pane>
        <el-tab-pane label="已发表" name="post">
          <blog_table state="1" :showEdit="true" :showDelete="true" :showRestore="false" :activeName="activeName"></blog_table>
        </el-tab-pane>
        <el-tab-pane label="草稿箱" name="draft">
          <blog_table state="0" :showEdit="true" :showDelete="true" :showRestore="false" :activeName="activeName"></blog_table>
        </el-tab-pane>
        <el-tab-pane label="回收站" name="dustbin">
          <blog_table state="2" :showEdit="false" :showDelete="true" :showRestore="true" :activeName="activeName"></blog_table>
        </el-tab-pane>
        <el-tab-pane label="博客管理" name="blogmana" v-if="isAdmin">
          <blog_table state="-2" :showEdit="false" :showDelete="true" :showRestore="false" :activeName="activeName"></blog_table>
        </el-tab-pane>
        <el-tab-pane label="博客配置" name="blogcfg">
          <blog_cfg></blog_cfg>
        </el-tab-pane>
      </el-tabs>
    </el-main>
  </el-container>
</template>
<script>
  import BlogTable from '@/components/BlogTable'
  import BlogCfg from '@/components/BlogCfg'
  import {postRequest} from '../utils/api'
  import {putRequest} from '../utils/api'
  import {deleteRequest} from '../utils/api'
  import {getRequest} from '../utils/api'
  export default {
    mounted: function () {
      var _this = this;
      getRequest("/isAdmin").then(resp=> {
        if (resp.status == 200) {
          _this.isAdmin = resp.data;
        }
      })
    },
    data() {
      return {
        // 初始化标签
        activeName: 'post',
        isAdmin: false
      };
    },
    methods: {
      // 这个啥也不做吗
      // 可能是自动做了
      handleClick(tab, event) {
       // console.log(tab, event);
      }
    },
    components: {
      'blog_table': BlogTable,
      'blog_cfg': BlogCfg
    }
  };
</script>
<style>
  .article_list > .header {
    background-color: #ececec;
    margin-top: 10px;
    padding-left: 5px;
    display: flex;
    justify-content: flex-start;
  }

  .article_list > .main {
    /*justify-content: flex-start;*/
    display: flex;
    flex-direction: column;
    padding-left: 0px;
    background-color: #fff;
    padding-top: 0px;
    margin-top: 8px;
  }
</style>
