package com.leyou.controller;

import com.leyou.client.*;
import com.leyou.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
    @Autowired
    TemplateEngine templateEngine;

    @RequestMapping("hello")
    public String hello(Model model){
        String name = "张三";
        model.addAttribute("name",name);
        return "hello";
    }

    @RequestMapping("item/{spuId}.html")
    public String item(@PathVariable("spuId") Long spuId,Model model){

        Spu spu = spuClient.findSpuBuId(spuId);

        SpuDetail spuDetail = spuClient.findSpuDetailBySpuId(spuId);

        Brand brand = brandClient.findBrandById(spu.getBrandId());

        List<Sku> skuList = skuClient.findSkuBySpuId(spuId);

        List<SpecGroup> groups = specGroupClient.findSpecGroupList(spu.getCid3());

        List<Category> categoryList = categoryClient.findCategoryByCids(
                Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));

        List<SpecParam> specParamList = specParamClient.findParamByCidAndGeneric(spu.getCid3(),false);

        Map<Long,String> paramMap = new HashMap<>();
        specParamList.forEach(param->{
            paramMap.put(param.getId(),param.getName());
        });

        model.addAttribute("spu",spu);
        model.addAttribute("spuDetail",spuDetail);
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("brand",brand);
        model.addAttribute("skuList",skuList);
        model.addAttribute("groups",groups);
        model.addAttribute("paramMap",paramMap);

        creatHtml(spu,spuDetail,categoryList,brand,skuList,groups,paramMap);

        return "item";
    }

    private void creatHtml(Spu spu, SpuDetail spuDetail, List<Category> categoryList, Brand brand, List<Sku> skuList, List<SpecGroup> groups, Map<Long, String> paramMap) {

        PrintWriter writer = null;
        try {
            Context context = new Context();
            context.setVariable("spu",spu);
            context.setVariable("spuDetail",spuDetail);
            context.setVariable("categoryList",categoryList);
            context.setVariable("brand",brand);
            context.setVariable("skuList",skuList);
            context.setVariable("groups",groups);
            context.setVariable("paramMap",paramMap);

            File file =new File("D:\\1906a\\pt1\\5.9\\nginx-1.16.1\\html\\"+spu.getId()+".html");
            writer = new PrintWriter(file);

            templateEngine.process("item",context,writer);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (writer!=null){
                writer.close();
            }
        }
    }
}
