package com.leyou.controller;

import com.leyou.client.*;
import com.leyou.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class GoodsDetailController {

    @Autowired
    SpuClient spuClient;
    @Autowired
    SkuClient skuClient;
    @Autowired
    SpecGroupClient specGroupClient;
    @Autowired
    CategoryClient categoryClient;
    @Autowired
    SpecParamClient specParamClient;
    @Autowired
    BrandClient brandClient;

    @RequestMapping("hello")
    public String hello(Model model){
        String name = "张三";
        model.addAttribute("name",name);
        return "hello";
    }

    @RequestMapping("item/{spuId}.html")
    public String item(@PathVariable("spuId") Long spuId,Model model){

        Spu spu = spuClient.findSpuBuId(spuId);
        model.addAttribute("spu",spu);

        SpuDetail spuDetail = spuClient.findSpuDetailBySpuId(spuId);
        model.addAttribute("spuDetail",spuDetail);

        List<Sku> skuList = skuClient.findSkuBySpuId(spuId);
        model.addAttribute("skuList",skuList);

        List<SpecGroup> specGroupList = specGroupClient.findSpecGroupList(spu.getCid3());
        model.addAttribute("specGroupList",specGroupList);

        List<Category> categoryList = categoryClient.findCategoryByCids(
                Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));
        model.addAttribute("categoryList",categoryList);

        List<SpecParam> specParamList = specParamClient.findParamByCidAndGeneric(spu.getCid3(),false);
        Map<Long,String> paramMap = new HashMap<>();
        specParamList.forEach(param->{
            paramMap.put(param.getId(),param.getName());
        });
        model.addAttribute("paramMap",paramMap);

        Brand brand = brandClient.findBrandById(spu.getBrandId());
        model.addAttribute("brand",brand);

        return "item";
    }
}
