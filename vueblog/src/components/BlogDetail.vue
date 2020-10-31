<template>
  <!--整个一大行?-->
  <el-row v-loading="loading">
    <el-col :span="24">
      <div style="text-align: left;">
        <!--向后跳转一个页面-->
        <el-button type="text" icon="el-icon-back" @click="goBack" style="padding-bottom: 0px;">返回</el-button>
      </div>
    </el-col>
    <el-col :span="24">
      <div>
        <div><h3 style="margin-top: 0px;margin-bottom: 0px">{{article.title}}</h3></div>
        <div style="width: 100%;margin-top: 5px;display: flex;justify-content: flex-end;align-items: center">
          <div style="display: inline; color: #20a0ff;margin-left: 50px;margin-right:20px;font-size: 12px;">
            {{article.nickname}}
          </div>
          <!--或许pageView是在后端++的-->
          <!--答:没错,就是在后端加的,直接在数据库里改的-->
          <span style="color: #20a0ff;margin-right:20px;font-size: 12px;">浏览 {{article.pageView==null?0:article.pageView}}</span>
          <span style="color: #20a0ff;margin-right:20px;font-size: 12px;"> {{article.editTime | formatDateTime}}</span>
          <!--添加了tag,还挺好看-->
          <el-tag type="success" v-for="(item,index) in article.tags" :key="index" size="small"
                  style="margin-left: 8px">{{item.tagName}}
          </el-tag>
          <span style="margin:0px 50px 0px 0px"></span>
        </div>
      </div>
    </el-col>
    <el-col>
      <!--内容, 应也是来自数据库属性, 名字就是htmlContent-->
      <div style="text-align: left" v-html="article.htmlContent">
      </div>
    </el-col>
  </el-row>
</template>
<script>
  import {getRequest} from '../utils/api'
  export default{
    methods: {
      goBack(){
        // 正为向后跳转,负为向后跳转
        this.$router.go(-1);
      }
    },
    mounted: function () {
      var aid = this.$route.query.aid;
      // 这个an变量没见到呀?
      this.activeName = this.$route.query.an;
      console.log(aid);
      // 确实不存在
      console.log(this.activeName);
      var _this = this;
      this.loading = true;
      getRequest("/article/" + aid).then(resp=> {
        if (resp.status == 200) {
          _this.article = resp.data;
        }
        _this.loading = false;
      }, resp=> {
        _this.loading = false;
        _this.$message({type: 'error', message: '页面加载失败!'});
      });
    },
    data(){
      return {
        article: {},
        loading: false,
        activeName: ''
      }
    }
  }
</script>
