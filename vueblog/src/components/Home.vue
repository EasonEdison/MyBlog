<template>
  <el-container class="home_container">
    <!--头不-->
    <el-header>
      <div class="home_title">V部落博客管理平台</div>
      <div class="home_userinfoContainer">
        <!--下拉框，里面的都是固定操作-->
        <el-dropdown @command="handleCommand">
        <span class="el-dropdown-link home_userinfo">
          <!--left，right还是没区别-->
          <!--这里有的参数-->
          {{currentUserName}}<i class="el-icon-arrow-down el-icon--right home_userinfo"></i>
        </span>
            <el-dropdown-menu slot="dropdown">
              <!--command可以在后面做判断的时候使用,算是一个注释-->
              <!--除了退出登录,其他功能都没有实现-->
              <el-dropdown-item command="sysMsg">系统消息</el-dropdown-item>
              <el-dropdown-item command="MyArticle">我的文章</el-dropdown-item>
              <el-dropdown-item command="MyHome">个人主页</el-dropdown-item>
              <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
      </div>
    </el-header>
    <!--主要内容-->
    <el-container>
      <el-aside width="200px">
        <!--左侧菜单-->
        <el-menu
          default-active="0"
          class="el-menu-vertical-demo" style="background-color: #ECECEC" router>
          <!--上面最后的router是干嘛的？答:控制使用index来跳转路由path的-->
          <!--对routes里面的内容遍历，但是不显示隐藏的-->
          <!--this.$router.options.routes是如何绑定的？-->
          <template v-for="(item,index) in this.$router.options.routes" v-if="!item.hidden">
            <!--如果孩子大于1，就显示子菜单-->
            <!--对应这个if-->
            <el-submenu :index="index+''" v-if="item.children.length>1" :key="index">
              <template slot="title">
                <!--根据自带的属性设置格式-->
                <i :class="item.iconCls"></i>
                <span>{{item.name}}</span>
              </template>
              <!--给子标签也显示出来-->
              <!--那为啥不是对应这个if呢?因为这个是在组件的里面?-->
              <el-menu-item v-for="child in item.children" v-if="!child.hidden" :index="child.path" :key="child.path">
                {{child.name}}
              </el-menu-item>
            </el-submenu>

            <!--这个else-->
            <template v-else>
              <!--但是这个怎么跳转呢?-->
              <!--index这里绑定了path, 为啥index绑定了就能跳?是因为el-menu-item的特性?-->
              <el-menu-item :index="item.children[0].path">
                <i :class="item.children[0].iconCls"></i>
                <span slot="title">{{item.children[0].name}}</span>
              </el-menu-item>
            </template>
          </template>
        </el-menu>
      </el-aside>
      <!--内容页面-->
      <el-container>
        <el-main>
          <!--面包屑,就最上面那层-->
          <!--这个class是管图标分隔符的-->
          <el-breadcrumb separator-class="el-icon-arrow-right">
            <!--path指的是路由的路径,这个跟面包屑没啥关系吧,就是基础语法-->
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <!--如果text中的内容存在,就也会显示,否则不显示;这个也没有点击功能-->
            <el-breadcrumb-item v-text="this.$router.currentRoute.name"></el-breadcrumb-item>

          </el-breadcrumb>
          <!--根据激活状态判断在哪里显示-->
          <keep-alive>
            <router-view v-if="this.$route.meta.keepAlive"></router-view>
          </keep-alive>
          <router-view v-if="!this.$route.meta.keepAlive"></router-view>
        </el-main>
      </el-container>
    </el-container>
  </el-container>
</template>
<script>
  import {getRequest} from '../utils/api'
  export default{
    methods: {
      handleCommand(command){
        var _this = this;
        if (command == 'logout') {
          this.$confirm('注销登录吗?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(function () {
            getRequest("/logout")
            _this.currentUserName = '游客';
            _this.$router.replace({path: '/'});
          }, function () {
            //取消
          })
        }
      }
    },
    // 钩子函数,转到的时候触发
    mounted: function () {
      this.$alert('为了确保所有的小伙伴都能看到完整的数据演示，数据库只开放了查询权限和部分字段的更新权限，其他权限都不具备，完整权限的演示需要大家在自己本地部署后，换一个正常的数据库用户后即可查看，这点请大家悉知!', '友情提示', {
        confirmButtonText: '确定',
        callback: action => {
        }
      });
      var _this = this;
      // function函数是子集的，参数是后端返回的
      getRequest("/currentUserName").then(function (msg) {
        _this.currentUserName = msg.data;
      }, function (msg) {
        _this.currentUserName = '游客';
      });
    },
    // 最后就返回了给名字
    data(){
      return {
        currentUserName: ''
      }
    }
  }
</script>
<style>
  .home_container {
    height: 100%;
    position: absolute;
    top: 0px;
    left: 0px;
    width: 100%;
  }

  .el-header {
    background-color: #20a0ff;
    color: #333;
    text-align: center;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .el-aside {
    background-color: #ECECEC;
  }

  .el-main {
    background-color: #fff;
    color: #000;
    text-align: center;
  }

  .home_title {
    color: #fff;
    font-size: 22px;
    display: inline;
  }

  .home_userinfo {
    color: #fff;
    cursor: pointer;
  }

  .home_userinfoContainer {
    display: inline;
    margin-right: 20px;
  }
</style>
