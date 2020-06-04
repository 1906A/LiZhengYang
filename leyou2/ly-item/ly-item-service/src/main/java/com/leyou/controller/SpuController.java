package com.leyou.controller;

import com.leyou.common.PageResult;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuDetail;
import com.leyou.service.SpuService;
import com.leyou.vo.SpuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("spu")
public class SpuController {

    @Autowired
    SpuService spuService;

    @RequestMapping("page")
    public PageResult<SpuVo> findSpuByPage(@RequestParam("key") String key,
                                          @RequestParam("page") Integer page,
                                          @RequestParam("rows") Integer rows,
                                          @RequestParam("saleable") Integer saleable){

        return spuService.findSpuByPage(key,saleable,page,rows);
    }

    @RequestMapping("saveOrUpdateGoods")
    public void saveSpuDetail(@RequestBody SpuVo spuVo){
        if (spuVo.getId()!=null){
            spuService.updateSpuDetail(spuVo);
        }else {
            spuService.saveSpuDetail(spuVo);
        }
    }

    @RequestMapping("detail/{spuId}")
    public SpuDetail findSpuDetailBySpuId(@PathVariable("spuId") Long spuId){
        return spuService.findSpuDetailBySpuId(spuId);
    }

    @RequestMapping("deleteById/{spuId}")
    public void deleteSpuBySpuId(@PathVariable("spuId") Long spuId){
        spuService.deleteSpuBySpuId(spuId);
    }

    @RequestMapping("upOrDown")
    public void upOrDown(@RequestParam("spuId") Long spuId,@RequestParam("saleable") int saleable){
        spuService.upOrDown(spuId,saleable);
    }

    @RequestMapping("findSpuBuId")
    public Spu findSpuBuId(@RequestParam("spuId")Long spuId){
        return spuService.findSpuBuId(spuId);
    }
}
