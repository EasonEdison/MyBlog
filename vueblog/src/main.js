import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
// import './styles/element-variables.scss'
import 'font-awesome/css/font-awesome.min.css'
// 再次导入了过滤器
import './utils/filter_utils.js'
import VCharts from 'v-charts'

Vue.use(ElementUI)
Vue.use(VCharts)

Vue.config.productionTip = false;
window.bus = new Vue();
new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: {App}
})
