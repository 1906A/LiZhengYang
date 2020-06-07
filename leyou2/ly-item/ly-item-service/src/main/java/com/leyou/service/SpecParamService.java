package com.leyou.service;

import com.leyou.dao.SpecParamMapper;
import com.leyou.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecParamService {

    @Autowired
    private SpecParamMapper specParamMapper;

    public void saveSpecParam(SpecParam specParam) {
        specParamMapper.insert(specParam);
    }

    public void updateSpecParam(SpecParam specParam) {
        specParamMapper.updateByPrimaryKey(specParam);
    }

    public void deleteSpecParamById(Long id) {
        specParamMapper.deleteByPrimaryKey(id);
    }

    public List<SpecParam> findSpecParamByCid(Long cid) {
        return specParamMapper.findSpecParamByCid(cid);
    }

    public List<SpecParam> findSpecParamByCidAndSearching(Long cid) {
        SpecParam specParam = new SpecParam();
        specParam.setCid(cid);
        specParam.setSearching(true);
        return specParamMapper.select(specParam);
    }

    public List<SpecParam> findParamByCidAndGeneric(Long cid, boolean generic) {
        SpecParam specParam = new SpecParam();
        specParam.setCid(cid);
        specParam.setGeneric(generic);
        return specParamMapper.select(specParam);
    }
}
