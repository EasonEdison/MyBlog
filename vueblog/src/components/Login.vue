<template>
  <!--v-bind:rules，是绑定一些数据？答：是绑定校验规则，通过prop的方法使用rules的校验规则-->
  <!--所有的class都可以在下面设置style-->
  <el-form :rules="rules" class="login-container" label-position="left"
           label-width="0px" v-loading="loading">
    <h3 class="login_title">系统登录</h3>
    <el-form-item prop="account">
      <!--输入用户名，关闭了自动提示。-->
      <el-input type="text" v-model="loginForm.username" auto-complete="off" placeholder="账号"></el-input>
    </el-form-item>
    <!--注意这个prop，这是用来设置校验规则的，-->
    <el-form-item prop="checkPass">
      <el-input type="password" v-model="loginForm.password" auto-complete="off" placeholder="密码"></el-input>
    </el-form-item>
    <!--这个左右对齐好像没差别-->
    <el-checkbox class="login_remember" v-model="checked" label-position="left">记住密码</el-checkbox>
    <el-form-item style="width: 100%">
      <!--v:on绑定自定义方法-->
      <el-button type="primary" @click.native.prevent="submitClick" style="width: 100%">登录</el-button>
    </el-form-item>
  </el-form>
</template>
<!--调用utils/api里面的HTTP传递方法-->
<script>
  import {postRequest} from '../utils/api'
  import {putRequest} from '../utils/api'
  export default{
    data(){
      // 下面这些值会传递给上边
      // 这个return是要传给后端码？
      return {
        // rules：规则，校验规则
        rules: {
          // 跟上面的prop绑定的，触发器为失去焦点
          account: [{required: true, message: '请输入用户名', trigger: 'blur'}],
          checkPass: [{required: true, message: '请输入密码', trigger: 'blur'}]
        },
        // 默认被选择
        checked: true,
        // 默认文本内容，因为由v-model，所以会显示
        loginForm: {
          username: 'sang',
          password: '123'
        },
        loading: false
      }
    },
    methods: {
      submitClick: function () {
        var _this = this;
        // 改变参数
        this.loading = true;
        // 方法里用的axios，第一个参数是url，第二个是参数
        // 方法通往Spring security的权限管理
        postRequest('/login', {
          username: this.loginForm.username,
          password: this.loginForm.password
        }).then(resp=> {
          _this.loading = false;
          if (resp.status == 200) {
            //成功
            var json = resp.data;
            if (json.status == 'success') {
              _this.$router.replace({path: '/home'});
            } else {
              // 第一个参数是内容，第二个参数是标题
              _this.$alert('登录失败!', '失败!');
            }
          } else {
            //失败
            _this.$alert('登录失败!', '失败!');
          }
        }, resp=> {
          _this.loading = false;
          _this.$alert('找不到服务器⊙﹏⊙∥!', '失败!');
        });
      }
    }
  }
</script>
<!--给绑定的class设置style-->
<style>
  .login-container {
    border-radius: 15px;
    background-clip: padding-box;
    margin: 180px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }

  .login_title {
    margin: 0px auto 40px auto;
    text-align: center;
    color: #505458;
  }

  .login_remember {
    margin: 0px 0px 35px 0px;
    text-align: left;
  }
</style>
