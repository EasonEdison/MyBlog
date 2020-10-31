'use strict'
// Template version: 1.2.7
// see http://vuejs-templates.github.io/webpack for documentation.

// 这个不知道有啥用
const path = require('path')

// 话说这边并没有用env: require()来指定环境啊
module.exports = {
  // 管开发的时候，既有前后端交互的跨域配置，也有在前端玩的配置
  dev: {
    // Paths
    // 静态资源子目录和公开地址
    assetsSubDirectory: 'static',
    assetsPublicPath: '/',
    // 这边是设置代理，解决跨域问题，开发的时候才用
    proxyTable: {
      // 路径啥也不加
      '/': {
        // 改写，相当于把http://localhost:8080改成下面的内容+/
        target: 'http://localhost:8082',
        changeOrigin: true, // 指示是否跨域
        pathRewrite: {
          '^/': ''  // 啥都没有，就等价于http://localhost:8081/
        }
      }
    },

    // 这边是前端自己玩
    // Various Dev Server settings
    host: 'localhost', // can be overwritten by process.env.HOST

    // dev-server的端口号
    port: 8080, // can be overwritten by process.env.PORT, if port is in use, a free one will be determined
    autoOpenBrowser: false,
    errorOverlay: true,
    notifyOnErrors: true,
    poll: false, // https://webpack.js.org/configuration/dev-server/#devserver-watchoptions-


    /**
     * Source Maps
     */

    // https://webpack.js.org/configuration/devtool/#development
    devtool: 'eval-source-map',


    // debug工具出问题的时候可以试试改为false，可能会有用
    cacheBusting: true,


    // 是否生成css、map文件，说可能存在问题，但是没啥必要用这个，出问题了可以控制台
    // 所以这里于默认不同，设为false
    cssSourceMap: false,

  },
  // 配置build、打包问题
  // 这一套很固定，根本不用动，创建完了就是这样的
  build: {
    // 这块好像都是说运行npm run build之后，生成的文件应在的位置
    // build之后生成的index位置？
    index: path.resolve(__dirname, '../dist/index.html'),

    // Paths
    // 静态资源的根目录
    assetsRoot: path.resolve(__dirname, '../dist'),
    // 静态资源子目录
    assetsSubDirectory: 'static',
    // 静态资源的公开路径，也就是真正的引用路径(引用路径指使用时？)
    assetsPublicPath: '/',

    /**
     * Source Maps
     */
    // 是否生成生产环境的sourcemap，sourcemap用来对编译后的文件进行debug，方法是映射回编译前的文件
    // 编译后的代码人是看不懂的
    productionSourceMap: true,

    // 这应当是一种映射工具
    devtool: '#source-map',


    // 是否在生产环境中压缩代码，如果要压缩必须安装compression-webpack-plugin
    productionGzip: false,
    // 指定要压缩的文件类型
    productionGzipExtensions: ['js', 'css'],


    // 开启编译完成后的报告，只有运行了npm run build --report才有吧
    bundleAnalyzerReport: process.env.npm_config_report
  }
}
