<template>
  <el-container v-loading="loading" class="post-article">
    <!--没有了head排版就乱了,这三个就不在一排了,成了上下关系-->
    <el-header class="header">
      <!--从数据库获取栏目对应id信息,显示还得看label-->
      <el-select v-model="article.cid" placeholder="请选择文章栏目" style="width: 150px;">
        <el-option
          v-for="item in categories"
          :key="item.id"
          :label="item.cateName"
          :value="item.id">
        </el-option>
      </el-select>
      <el-input v-model="article.title" placeholder="请输入标题..." style="width: 400px;margin-left: 10px"></el-input>
      <!--已经添加的先在前面显示-->
      <!--closable是可移除标签-->
      <!--:disable-transitions="false" 开启渐变动画(是一个翻转的效果)-->
      <!--调用handleClose移除标签-->
      <el-tag
        :key="tag"
        v-for="tag in article.dynamicTags"
        closable
        :disable-transitions="false"
        @close="handleClose(tag)" style="margin-left: 10px">
        {{tag}}
      </el-tag>
      <!--这个也在前面,是点击了button之后才展示-->
      <!--可以通过回车键确定，也可以失去焦点确定-->
      <el-input
        class="input-new-tag"
        v-if="tagInputVisible"
        v-model="tagValue"
        ref="saveTagInput"
        size="small"
        @keyup.enter.native="handleInputConfirm"
        @blur="handleInputConfirm">
      </el-input>
      <!--这一套都是固定的-->
      <el-button v-else class="button-new-tag" type="primary" size="small" @click="showInput">+Tag</el-button>
    </el-header>
    <el-main class="main">
      <div id="editor">
        <!--用的库,但是图像的两个方法是自己定义的-->
        <mavon-editor style="height: 100%;width: 100%;" ref=md @imgAdd="imgAdd"
                      @imgDel="imgDel" v-model="article.mdContent"></mavon-editor>
      </div>
      <div style="display: flex;align-items: center;margin-top: 15px;justify-content: flex-end">
        <!--这是修改才有的button-->
        <el-button @click="cancelEdit" v-if="from!=undefined">放弃修改</el-button>
        <!--如果是新的或者是草稿-->
        <template v-if="from==undefined || from=='draft'">
          <el-button @click="saveBlog(0)">保存到草稿箱</el-button>
          <el-button type="primary" @click="saveBlog(1)">发表文章</el-button>
        </template>
        <template v-else="from==post">
          <el-button type="primary" @click="saveBlog(1)">保存修改</el-button>
        </template>
      </div>
    </el-main>
  </el-container>
</template>
<script>
  import {postRequest} from '../utils/api'
  import {putRequest} from '../utils/api'
  import {deleteRequest} from '../utils/api'
  import {getRequest} from '../utils/api'
  import {uploadFileRequest} from '../utils/api'
  // Local Registration
  import {mavonEditor} from 'mavon-editor'
  // 可以通过 mavonEditor.markdownIt 获取解析器markdown-it对象
  import 'mavon-editor/dist/css/index.css'
  import {isNotNullORBlank} from '../utils/utils'

  export default {
    mounted: function () {
      // 获取所有专栏
      this.getCategories();
      // 状态设置
      var from = this.$route.query.from;
      this.from = from;
      var _this = this;
      if (from != null && from != '' && from != undefined) {
        var id = this.$route.query.id;
        this.id = id;
        this.loading = true;
        getRequest("/article/" + id).then(resp=> {
          _this.loading = false;
          // 获取已有的tag
          if (resp.status == 200) {
            _this.article = resp.data;
            var tags = resp.data.tags;
            _this.article.dynamicTags = []
            for (var i = 0; i < tags.length; i++) {
              _this.article.dynamicTags.push(tags[i].tagName)
            }
          } else {
            _this.$message({type: 'error', message: '页面加载失败!'})
          }
        }, resp=> {
          _this.loading = false;
          _this.$message({type: 'error', message: '页面加载失败!'})
        })
      }
    },
    components: {
      mavonEditor
    },
    methods: {
      cancelEdit(){
        this.$router.go(-1)
      },
      saveBlog(state){
        if (!(isNotNullORBlank(this.article.title, this.article.mdContent, this.article.cid))) {
          this.$message({type: 'error', message: '数据不能为空!'});
          return;
        }
        // 将所有的信息都重新整一遍，估计后端那边接收的是一个Article类
        var _this = this;
        _this.loading = true;
        postRequest("/article/", {
          id: _this.article.id,
          title: _this.article.title,
          mdContent: _this.article.mdContent,
          htmlContent: _this.$refs.md.d_render,
          cid: _this.article.cid,
          state: state,
          dynamicTags: _this.article.dynamicTags
        }).then(resp=> {
          _this.loading = false;
          if (resp.status == 200 && resp.data.status == 'success') {
            _this.article.id = resp.data.msg;
            _this.$message({type: 'success', message: state == 0 ? '保存成功!' : '发布成功!'});
//            if (_this.from != undefined) {
            window.bus.$emit('blogTableReload')
//            }
            if (state == 1) {
              _this.$router.replace({path: '/articleList'});
            }
          }
        }, resp=> {
          _this.loading = false;
          _this.$message({type: 'error', message: state == 0 ? '保存草稿失败!' : '博客发布失败!'});
        })
      },
      imgAdd(pos, $file){
        var _this = this;
        // 第一步.将图片上传到服务器.
        var formdata = new FormData();
        formdata.append('image', $file);
        uploadFileRequest("/article/uploadimg", formdata).then(resp=> {
          var json = resp.data;
          if (json.status == 'success') {
           // _this.$refs.md.$imgUpdateByUrl(pos, json.msg)
            _this.$refs.md.$imglst2Url([[pos, json.msg]])
          } else {
            _this.$message({type: json.status, message: json.msg});
          }
        });
      },
      // 删除就是啥也不做？
      imgDel(pos){

      },
      getCategories(){
        let _this = this;
        getRequest("/admin/category/all").then(resp=> {
          _this.categories = resp.data;
        });
      },
      handleClose(tag) {
        this.article.dynamicTags.splice(this.article.dynamicTags.indexOf(tag), 1);
      },
      showInput() {
        this.tagInputVisible = true;
        this.$nextTick(_ => {
          this.$refs.saveTagInput.$refs.input.focus();
        });
      },
      handleInputConfirm() {
        let tagValue = this.tagValue;
        if (tagValue) {
          this.article.dynamicTags.push(tagValue);
        }
        this.tagInputVisible = false;
        this.tagValue = '';
      }
    },
    data() {
      return {
        categories: [],
        tagInputVisible: false,
        tagValue: '',
        loading: false,
        from: '',
        article: {
          id: '-1',
          dynamicTags: [],
          title: '',
          mdContent: '',
          cid: ''
        }
      }
    }
  }
</script>
<style>
  .post-article > .main > #editor {
    width: 100%;
    height: 450px;
    text-align: left;
  }

  .post-article > .header {
    background-color: #ececec;
    margin-top: 10px;
    padding-left: 5px;
    display: flex;
    justify-content: flex-start;
  }

  .post-article > .main {
    /*justify-content: flex-start;*/
    display: flex;
    flex-direction: column;
    padding-left: 5px;
    background-color: #ececec;
    padding-top: 0px;
  }

  .post-article > .header > .el-tag + .el-tag {
    margin-left: 10px;
  }

  .post-article > .header > .button-new-tag {
    margin-left: 10px;
    height: 32px;
    line-height: 30px;
    padding-top: 0;
    padding-bottom: 0;
  }

  .post-article > .header > .input-new-tag {
    width: 90px;
    margin-left: 10px;
    vertical-align: bottom;
  }

  .post-article {
  }
</style>
