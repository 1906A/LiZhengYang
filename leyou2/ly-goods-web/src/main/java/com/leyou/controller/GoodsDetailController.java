package com.leyou.controller;

import com.leyou.client.SkuClient;
import com.leyou.client.SpecGroupClient;
import com.leyou.client.SpuClient;
import com.leyou.pojo.Sku;
import com.leyou.pojo.SpecGroup;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class GoodsDetailController {

    @Autowired
    SpuClient spuClient;
    @Autowired
    SkuClient skuClient;
    @Autowired
    SpecGroupClient specGroupClient;

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



        return "item";
    }
}
