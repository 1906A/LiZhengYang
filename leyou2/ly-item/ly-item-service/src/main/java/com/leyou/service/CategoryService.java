package com.leyou.service;

import com.leyou.dao.CategoryMapper;
import com.leyou.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    public List<Category> findCategory(Category category){
        return categoryMapper.select(category);
    }

    public Category findCate(int id){
        return categoryMapper.findCate(id);
    }

    public void categoryAdd(Category category) {
        categoryMapper.insertSelective(category);
     }

    public void categoryUpdate(Category category) {
        categoryMapper.updateByPrimaryKey(category);
    }

    public void deleteById(Long id) {
        Category category = new Category();
        category.setId(id);
        categoryMapper.deleteByPrimaryKey(category);
    }

    public Category findCategoryById(Long id) {
       return categoryMapper.selectByPrimaryKey(id);
    }
}
