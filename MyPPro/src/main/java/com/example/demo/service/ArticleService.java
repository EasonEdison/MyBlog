package com.example.demo.service;

import com.example.demo.bean.Article;
import com.example.demo.mapper.ArticleMapper;
import com.example.demo.mapper.TagsMapper;
import javafx.util.converter.TimeStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional
public class ArticleService {

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    TagsMapper tagsMapper;

    public String stripHtml(String content) {
        content = content.replaceAll("<p .*?>", "");
        content = content.replaceAll("<br\\s*/?>", "");
        content = content.replaceAll("\\<.*?>", "");
        return content;
    }

    // 给文章添加标签，对数据库操作的
    public int addTagsToArticle(String[] dynamicTags, Long aid){
        // 1. 删除该文章目前所有的标签
        tagsMapper.deleteTagsByAid(aid);
        // 2. 新来的标签里面可能有新类型，传入数据库
        tagsMapper.saveTags(dynamicTags);
        // 3. 查询这些标签的id
        List<Long> tIds = tagsMapper.getTagsIdByTagName(dynamicTags);
        // 4. 重新给文章设置标签
        int i = tagsMapper.saveTags2ArticleTags(tIds, aid);
        return i == dynamicTags.length ? i : -1;
    }

    public int addNewArticle(Article article){
        // 处理文章摘要，如果没写摘要就截取前50个字符
        if (article.getSummary()==null || "".equals(article.getSummary())){
            // 先给处理一下
            String stripHtml = stripHtml(article.getHtmlContent());
            // 全部或前50
            article.setSummary(stripHtml.substring(0, stripHtml.length() > 50 ? 50 : stripHtml.length()));
        }

        // 看有无指定id，没有的前端默认是-1
        // article: {
        //     id: '-1',
        //             dynamicTags: [],
        //     title: '',
        //             mdContent: '',
        //             cid: ''
        // }
        if (article.getId() == -1){
            // 添加操作
            // 设置时间
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            if (article.getState() == 1){
                article.setPublishDate(timestamp);
            }
            article.setEditTime(timestamp);
            // 设置用户
            article.setUid(Utils.getCurrentUser().getId());
            // 向数据库添加文章
            int i = articleMapper.addNewArticle(article);
            // 向数据库添加文章绑定的标签, 因为数据库里面Article是跟标签无关的
            String[] dynamicTags = article.getDynamicTags();
            if (dynamicTags != null && dynamicTags.length > 0){
                int tags = addTagsToArticle(dynamicTags, article.getId());
                // 失败为-1
                if (tags == -1){
                    return tags;
                }
            }
            return i;
        } else {
            // 自定义了id
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            if (article.getState() == 1){
                article.setPublishDate(timestamp);
            }
            article.setEditTime(timestamp);
            int i = articleMapper.addNewArticle(article);
            String[] dynamicTags = article.getDynamicTags();
            if (dynamicTags != null && dynamicTags.length > 0){
                int tags = addTagsToArticle(dynamicTags, article.getId());
                if (tags == -1){
                    return tags;
                }
            }
            return i;
        }
    }

    public List<Article> getArticleByState(Integer state, Integer page, Integer count, String keywords){
        // start是自己算的
        int start = (page - 1) * count;
        Long uid = Utils.getCurrentUser().getId();
        return articleMapper.getArticleByState(state, start, count, uid, keywords);
    }

    public int getArticleCountByState(Integer state, Long uid, String keywords){
        return articleMapper.getArticleCountByState(state, uid, keywords);
    }

    // 删除，如果已经是回收站文章，就彻底删除；如果不在回收站，就放入回收站
    public int updateArticleState(Long[] aids, Integer state){
        if (state == 2){
            return articleMapper.deleteArticleById(aids);
        } else {
            return articleMapper.updateArticleState(aids, 2);
        }
    }

    // 从回收站恢复
    public int restoreArticle(Integer articleId){
        return articleMapper.updateArticleStateById(articleId, 1);
    }

    // 通过id获取文章，也就是读取了，每次读取浏览量都+1
    public Article getArticleById(Long aid){
        Article article = articleMapper.getArticleById(aid);
       articleMapper.pvIncrement(aid);
       return article;
    }

    public void pvStatisticsPerDay(){
        articleMapper.pvStatisticsPerDay();
    }

    // 获取访问最近的七天
    public List<String> getCategories(){
        return articleMapper.getCategories(Utils.getCurrentUser().getId());
    }

    // 获取访问最近的七天的访问量
    public List<Integer> getDataStatistics(){
        return articleMapper.getDataStatistics(Utils.getCurrentUser().getId());
    }

}
