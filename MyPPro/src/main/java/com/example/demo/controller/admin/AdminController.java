package com.example.demo.controller.admin;

import com.example.demo.bean.Article;
import com.example.demo.bean.ResponBean;
import com.example.demo.service.ArticleService;
import com.example.demo.service.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")   // 都是带admin前缀的
public class AdminController {
    @Autowired
    ArticleService articleService;

    @RequestMapping(value = "/article/all", method = RequestMethod.GET)
    public Map<String, Object> getArticleByState(@RequestParam(value = "state", defaultValue = "-1") Integer state,
                                                 @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                 @RequestParam(value = "count", defaultValue = "6") Integer count,
                                                 String keywords){
        List<Article> articles = articleService.getArticleByState(-2, page, count, keywords);
        Map<String, Object> map = new HashMap<>();
        map.put("articles", articles);
        // 所有人的都能看到，不用管id，只要正常状态的
        map.put("totalCount", articleService.getArticleCountByState(1, null, keywords));
        return map;
    }

    @RequestMapping(value = "/article/dustbin", method = RequestMethod.PUT)
    public ResponBean updateArticleState(Long[] aids, Integer state){
        if (articleService.updateArticleState(aids, state) == aids.length){
            return new ResponBean("success", "删除成功！");
        }
        return new ResponBean("error", "删除失败！");
    }
}
