package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagsMapper {

    int deleteTagsByAid(Long aid);

    int saveTags(@Param("tags") String[] tags);

    // 通过多个名字标签获取多个id，类似批量操作
    List<Long> getTagsIdByTagName(@Param("tagNames") String[] tagNames);

    // 绑定文章和标签
    int saveTags2ArticleTags(@Param("tagIds") List<Long> tagIds, @Param("aid") Long aid);
}
