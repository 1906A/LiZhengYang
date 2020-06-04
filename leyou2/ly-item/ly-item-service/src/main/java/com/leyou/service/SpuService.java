package com.leyou.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.PageResult;
import com.leyou.dao.SkuMapper;
import com.leyou.dao.SpuDetailMapper;
import com.leyou.dao.SpuMapper;
import com.leyou.dao.StockMapper;
import com.leyou.pojo.Sku;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuDetail;
import com.leyou.pojo.Stock;
import com.leyou.vo.SpuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SpuService {

    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;


    public PageResult<SpuVo> findSpuByPage(String key, Integer saleable, Integer page, Integer rows) {
        PageHelper.startPage(page,rows);
        PageInfo<SpuVo> list = new PageInfo<SpuVo>(spuMapper.findSpuByPage(key,saleable));
        return new PageResult<SpuVo>(list.getTotal(),list.getList());
    }

    public void saveSpuDetail(SpuVo spuVo) {

        Date nowDate = new Date();

        Spu spu=new Spu();
        spu.setTitle(spuVo.getTitle());
        spu.setSubTitle(spuVo.getSubTitle());
        spu.setBrandId(spuVo.getBrandId());
        spu.setCid1(spuVo.getCid1());
        spu.setCid2(spuVo.getCid2());
        spu.setCid3(spuVo.getCid3());
        spu.setSaleable(false);
        spu.setValid(true);
        spu.setCreateTime(nowDate);
        spu.setLastUpdateTime(nowDate);
        spuMapper.insert(spu);

        SpuDetail spuDetail = spuVo.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        spuDetailMapper.insert(spuDetail);

        List<Sku> skus = spuVo.getSkus();
        skus.forEach(sku -> {
            sku.setSpuId(spu.getId());
            sku.setEnable(true);
            sku.setCreateTime(nowDate);
            sku.setLastUpdateTime(nowDate);
            skuMapper.insert(sku);

            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockMapper.insert(stock);
        });
    }

    public SpuDetail findSpuDetailBySpuId(Long spuId) {
        return spuDetailMapper.selectByPrimaryKey(spuId);
    }

    public void updateSpuDetail(SpuVo spuVo) {

        Date nowDate = new Date();

        spuVo.setCreateTime(null);
        spuVo.setLastUpdateTime(nowDate);
        spuVo.setSaleable(null);
        spuVo.setValid(null);
        spuMapper.updateByPrimaryKeySelective(spuVo);

        SpuDetail spuDetail = spuVo.getSpuDetail();
        spuDetail.setSpuId(spuVo.getId());
        spuDetailMapper.updateByPrimaryKeySelective(spuDetail);

        List<Sku> skus = spuVo.getSkus();
        skus.forEach(s ->{
            s.setEnable(false);
            skuMapper.updateByPrimaryKey(s);
            stockMapper.deleteByPrimaryKey(s.getId());
        });

        List<Sku> skus1 = spuVo.getSkus();
        skus.forEach(sku -> {
            sku.setSpuId(spuVo.getId());
            sku.setEnable(true);
            sku.setCreateTime(nowDate);
            sku.setLastUpdateTime(nowDate);
            skuMapper.insert(sku);

            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockMapper.insert(stock);
        });
    }

    public void deleteSpuBySpuId(Long spuId) {
        List<Sku> skuList = skuMapper.findSkuBySpuId(spuId);
        skuList.forEach(s ->{
            s.setEnable(false);
            skuMapper.updateByPrimaryKeySelective(s);
            stockMapper.deleteByPrimaryKey(s.getId());
        });
        spuDetailMapper.deleteByPrimaryKey(spuId);
        spuMapper.deleteByPrimaryKey(spuId);
    }

    public void upOrDown(Long spuId,int saleable) {
        Spu spu = new Spu();
        spu.setId(spuId);
        spu.setSaleable(saleable ==1? true :false);
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    public Spu findSpuBuId(Long spuId) {
        return spuMapper.selectByPrimaryKey(spuId);
    }
}
