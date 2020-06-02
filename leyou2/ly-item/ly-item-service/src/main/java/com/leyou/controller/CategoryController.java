package com.leyou.controller;

import com.leyou.pojo.Category;
import com.leyou.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @RequestMapping("list")
    public List<Category> list(@RequestParam("pid") Long pid){
        Category category = new Category();
        category.setParentId(pid);
        return categoryService.findCategory(category);
    }

    @RequestMapping("id")
    public Object findCate(){
        return categoryService.findCate(6);
    }

    @RequestMapping("add")
    public String add(@RequestBody Category category){
        String result = "SUCC";
        try {
            categoryService.categoryAdd(category);
        }catch (Exception e){
            System.out.println("添加商品分类异常");
            result = "FALL";
        }
        return result;
    }

    @RequestMapping("update")
    public String update(@RequestBody Category category){
        String result = "SUCC";
        try {
            categoryService.categoryUpdate(category);
        }catch (Exception e){
            System.out.println("修改商品分类异常");
            result = "FALL";
        }
        return result;
    }

    @RequestMapping("deleteById")
    public String deleteById(@RequestParam("id") Long id){
        String result = "SUCC";
        try {
            categoryService.deleteById(id);
        }catch (Exception e){
            System.out.println("删除商品分类异常");
            result = "FALL";
        }
        return result;
    }

    @RequestMapping("findCategoryById")
    public Category findCategoryById(@RequestParam("id") Long id){
        return categoryService.findCategoryById(id);
    }
}
