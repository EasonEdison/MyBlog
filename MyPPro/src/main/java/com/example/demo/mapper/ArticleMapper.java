package com.example.demo.mapper;

import com.example.demo.bean.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleMapper {
    int addNewArticle(Article article);

    int updateArticle(Article article);

    // 分页获取？
    List<Article> getArticleByState(@Param("state") Integer state, @Param("start") Integer start,
                                    @Param("count") Integer count, @Param("uid") Long uid,
                                    @Param("keywords") String keywords);

    // 计数，各种参数
    int getArticleCountByState(@Param("state") Integer state, @Param("uid") Long uid,
                               @Param("keywords") String keywords);

    int updateArticleStateById(@Param("articleId") Integer articleId, @Param("state") Integer state);

    int updateArticleState(@Param("aids") Long[] aids, @Param("state") Integer state);

    int deleteArticleById(@Param("aids") Long[] aids);

    Article getArticleById(Long aid);

    // 访问量增加
    void pvIncrement(Long aid);

    // 每日统计
    void pvStatisticsPerDay();

    List<String> getCategories(Long uid);

    List<Integer> getDataStatistics(Long uid);
}

