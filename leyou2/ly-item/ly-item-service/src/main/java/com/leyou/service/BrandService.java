package com.leyou.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.PageResult;
import com.leyou.dao.BrandMapper;
import com.leyou.pojo.Brand;
import com.leyou.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService {

    @Autowired
    BrandMapper brandMapper;

    public PageResult<Brand> findBrand(String key, Integer page, Integer rows, String sortBy, boolean desc) {

        PageHelper.startPage(page,rows);

        List<Brand> brandList = brandMapper.findBrand(key,sortBy,desc);

        PageInfo<Brand> pageInfo = new PageInfo<Brand>(brandList);

        return new PageResult<Brand>(pageInfo.getTotal(),pageInfo.getList());
    }

    public PageResult<Brand> findBrandByLimit(String key, Integer page, Integer rows, String sortBy, boolean desc) {

      //  PageHelper.startPage(page,rows);

        List<Brand> brandList = brandMapper.findBrandLimit(key,(page-1)*rows,rows,sortBy,desc);

        Long brandCount = brandMapper.findBrandCount(key,sortBy,desc);

       // PageInfo<Brand> pageInfo = new PageInfo<Brand>(brandList);

        return new PageResult<Brand>(brandCount,brandList);
    }


    public void brandCategorySave(Brand brand, List<Long> cids) {

        brandMapper.insert(brand);

        cids.forEach(id ->{
            brandMapper.addBrandAndCategory(brand.getId(),id);
        });
    }

    public void deleteById(Long id) {
        Brand brand = new Brand();
        brand.setId(id);
        brandMapper.deleteByPrimaryKey(brand);
        brandMapper.deleteBrandAndCategory(id);
    }

    public List<Category> findCategoryByBrandId(Long pid) {

        return brandMapper.findCategoryByBrandId(pid);
    }

    public void updateBrand(Brand brand, List<Long> cids) {
        brandMapper.updateByPrimaryKey(brand);
        brandMapper.deleteBrandAndCategory(brand.getId());
        cids.forEach(cid ->{
            brandMapper.addBrandAndCategory(brand.getId(),cid);
        });
    }

    public List<Brand> findBrandBycid(Long cid) {
        return brandMapper.findBrandBycid(cid);
    }
}
