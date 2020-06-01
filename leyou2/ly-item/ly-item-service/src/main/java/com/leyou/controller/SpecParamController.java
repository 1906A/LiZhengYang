package com.leyou.controller;

import com.leyou.pojo.SpecParam;
import com.leyou.service.SpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("specParam")
public class SpecParamController {

    @Autowired
    private SpecParamService specParamService;

    @RequestMapping("param")
    public void saveSpecParam(@RequestBody SpecParam specParam){
        if (specParam.getId()==null){
            specParamService.saveSpecParam(specParam);
        }else {
            specParamService.updateSpecParam(specParam);
        }
    }

    @RequestMapping("param/{id}")
    public void deleteSpecParamById(@PathVariable("id") Long id){
        specParamService.deleteSpecParamById(id);
    }

    @RequestMapping("params")
    public List<SpecParam> findSpecParamByCid(@RequestParam("cid") Long cid){
        return specParamService.findSpecParamByCid(cid);
    }

    @RequestMapping("paramByCid")
    public List<SpecParam> findSpecParamByCidAndSearching(@RequestParam("cid") Long cid){
        return specParamService.findSpecParamByCidAndSearching(cid);
    }
}
