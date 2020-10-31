'use strict';
// 引入webpack的merge插件，顾名思义是合并用的，合并配置文件，相同的内容会被覆盖
const merge = require('webpack-merge');
// 导入配置文件
const prodEnv = require('./prod.env');
// 两个一合并，最终NODE_ENV的值为development，这不多此一举吗
module.exports = merge(prodEnv, {
  NODE_ENV: '"development"'
})
