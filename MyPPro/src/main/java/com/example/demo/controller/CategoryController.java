package com.example.demo.controller;

import com.example.demo.bean.Category;
import com.example.demo.bean.ResponBean;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Category> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @RequestMapping(value = "/{ids}", method = RequestMethod.DELETE)
    public ResponBean deleteById(@PathVariable String ids){
        boolean result = categoryService.deleteCategoryByIds(ids);
        if (result) {
            return new ResponBean("success", "删除成功！");
        }
        return new ResponBean("error", "删除失败！");
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponBean addNewCate(Category category){
        if ("".equals(category.getCateName()) || category.getCateName()==null){
            return new ResponBean("error", "请输入栏目名称！");
        }

        int result = categoryService.addCategory(category);

        if (result == 1){
            return new ResponBean("success", "添加成功！");
        }
        return new ResponBean("error", "添加失败！");
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public ResponBean updateCate(Category category){
        int i = categoryService.updateCategoryById(category);
        if (i==1){
            return new ResponBean("success","修改成功！");
        }
        return new ResponBean("error", "修改失败！");
    }
}
