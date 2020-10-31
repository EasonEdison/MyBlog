package com.example.demo.service;

import com.example.demo.bean.Category;
import com.example.demo.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional
public class CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    public List<Category> getAllCategories(){
        return categoryMapper.getAllCategories();
    }

    public boolean deleteCategoryByIds(String ids){
        String[] split = ids.split(",");
        int result = categoryMapper.deleteCategoryByIds(split);
        return result == split.length;
    }

    // 直接传进来新的栏目，然后根据id对着新的修改之前的
    public int updateCategoryById(Category category){
        return categoryMapper.updateCategoryById(category);
    }

    public int addCategory(Category category){
        category.setDate(new Timestamp(System.currentTimeMillis()));
        return categoryMapper.addCategory(category);
    }
}
