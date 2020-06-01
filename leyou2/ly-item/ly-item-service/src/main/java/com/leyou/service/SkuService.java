package com.leyou.service;

import com.leyou.dao.SkuMapper;
import com.leyou.pojo.Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkuService {

    @Autowired
    private SkuMapper skuMapper;

    public List<Sku> findSkuBySpuId(Long id) {
        return skuMapper.findSkuBySpuId(id);
    }
}
