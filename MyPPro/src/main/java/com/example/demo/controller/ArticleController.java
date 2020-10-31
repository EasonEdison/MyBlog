package com.example.demo.controller;

import com.example.demo.bean.Article;
import com.example.demo.bean.ResponBean;
import com.example.demo.service.ArticleService;
import com.example.demo.service.Utils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.nio.ch.IOUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/article")
public class ArticleController {
    // 统一格式
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    @Autowired
    ArticleService articleService;

    @RequestMapping(value = "/" ,method = RequestMethod.POST)
    public ResponBean addNewArticle(Article article){
        int result = articleService.addNewArticle(article);
        if (result == 1){
            return new ResponBean("success", article.getId()+"");
        } else {
            // 如果是草稿箱就是保存，如果是正常状态就是发表
            return new ResponBean("error", article.getState()==0? "文章保存失败！" : "文章发表失败！");
        }
    }

    // 上传图片。。。
    @RequestMapping(value = "/uploadimg", method = RequestMethod.POST)
    public ResponBean uploadImg(HttpServletRequest req, MultipartFile image){
        // 本地路径的相对路径，保存图片
        String filePath = "/blogImg/" +sdf.format(new Date());
        // 结合应用的路径，tomcat
        String imgFolderPath = req.getServletContext().getRealPath(filePath);
        // 创建目录
        File imgFolder = new File(imgFolderPath);
        if (!imgFolder.exists()){
            imgFolder.mkdirs();
        }

        // 自己写url，这个url就是能访问到服务器图片的地址
        StringBuffer url = new StringBuffer();
        url.append(req.getScheme()) // 协议名
            .append("://")
            .append(req.getServerName())    // 服务器名
            .append(":")
            .append(req.getServerPort())    // 端口名
            .append(req.getContextPath())   // 项目应用名，如果是根目录，就是空
            .append(filePath);              // 前面的都可以转化为服务器路径，然后加上相对路径文件夹
        // 创建新文件名, 使用UUID+原本名，原本名要清除空格
        String imgName = UUID.randomUUID() + "_" + image.getOriginalFilename().replaceAll(" ", "");
        try {
            // 将文件写到本地，第一个参数是要复制的对象，第二个是要输出的流
            IOUtils.write(image.getBytes(), new FileOutputStream(new File(imgFolder, imgName)));
            // 将文件名添加到url中
            url.append("/").append(imgName);
            return new ResponBean("success", url.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponBean("error", "上传失败！");
    }


    // 前短接收后的处理
    // if (resp.status == 200) {
    //     _this.articles = resp.data.articles;
    //     _this.totalCount = resp.data.totalCount;
    // }
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Map<String, Object> getArticleByState(@RequestParam(value = "state", defaultValue = "-1") Integer state,
                                                 @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                 @RequestParam(value = "count", defaultValue = "6") Integer count,
                                                 String keywords){
        // 数量
        int totalCount = articleService.getArticleCountByState(state, Utils.getCurrentUser().getId(), keywords);
        // 文章
        List<Article> articles = articleService.getArticleByState(state, page, count, keywords);
        // 保存成Map，要跟前端对应起来
        Map<String, Object> map = new HashMap<>();
        map.put("totalCount", totalCount);
        map.put("articles", articles);
        return map;
    }

    @RequestMapping(value = "/{aid}", method = RequestMethod.GET)
    public Article getArticleById(@PathVariable Long aid){
        return articleService.getArticleById(aid);
    }

    @RequestMapping(value = "/dustbin", method = RequestMethod.PUT)
    public ResponBean updateArticleState(Long[] aids, Integer state){
        if (articleService.updateArticleState(aids, state) == aids.length){
            return new ResponBean("success", "删除成功！");
        }
        return new ResponBean("error", "删除失败！");
    }

    @RequestMapping(value = "/restore", method = RequestMethod.PUT)
    public ResponBean restoreArticle(Integer articleId){
        if (articleService.restoreArticle(articleId) == 1){
            return new ResponBean("success", "还原成功！");
        }
        return new ResponBean("error", "还原失败！");
    }

    // getRequest("/article/dataStatistics").then(resp=> {
    //     if (resp.status == 200) {
    //         _this.$refs.dschart.options.xAxis.data = resp.data.categories;
    //         _this.$refs.dschart.options.series[0].data = resp.data.ds;
    //     }
    @RequestMapping("/dataStatistics")
    public Map<String, Object> dataStatistics(){
        Map<String, Object> map = new HashMap<>();
        List<String> categories = articleService.getCategories();
        List<Integer> dataStatistics = articleService.getDataStatistics();
        map.put("categories", categories);
        map.put("ds", dataStatistics);
        return map;
    }
}
