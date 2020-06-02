package com.leyou.controller;

import com.leyou.common.PageResult;
import com.leyou.pojo.Brand;
import com.leyou.pojo.Category;
import com.leyou.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    BrandService brandService;

    @RequestMapping("page")
    public Object findBrandByPage(@RequestParam("key") String key,
                                  @RequestParam("page") Integer page,
                                  @RequestParam("rows") Integer rows,
                                  @RequestParam(required = false,value = "sortBy") String sortBy,
                                  @RequestParam(required = false,value = "desc") boolean desc){
        System.out.println(key+"=="+page+"=="+rows+"=="+sortBy+"=="+desc);

        //PageResult<Brand> brandList = brandService.findBrand(key,page,rows,sortBy,desc);
        PageResult<Brand> brandList = brandService.findBrandByLimit(key,page,rows,sortBy,desc);
        return brandList;
    }

    @RequestMapping("addOrEditBrand")
    public void addOrEditBrand(Brand brand,
                               @RequestParam(required = false,value = "cids") List<Long> cids){
        if (brand.getId()!=null){
            brandService.updateBrand(brand,cids);
        }else {
            brandService.brandCategorySave(brand,cids);
        }

    }

    @RequestMapping("deleteById/{id}")
    public void deleteById(@PathVariable("id") Long id){
        brandService.deleteById(id);
    }

    @RequestMapping("bid/{id}")
    public List<Category> findCategoryByBrandId(@PathVariable("id") Long pid){
        return brandService.findCategoryByBrandId(pid);
    }

    @RequestMapping("cid/{cid}")
    public List<Brand> findBrandBycid(@PathVariable("cid") Long cid){
        return brandService.findBrandBycid(cid);
    }

    @RequestMapping("findBrandById")
    public Brand findBrandById(@RequestParam("id") Long id){
        return brandService.findBrandById(id);
    }
}
