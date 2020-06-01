package com.leyou.search;

import com.leyou.common.PageResult;
import com.leyou.search.client.SpuClient;
import com.leyou.search.item.Goods;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.GoodService;
import com.leyou.vo.SpuVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LySearchApplicationTests {

    @Autowired
    SpuClient spuClient;
    @Autowired
    GoodService goodService;
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    GoodsRepository goodsRepository;

    @Test
    public void contextLoads() {

        elasticsearchTemplate.createIndex(Goods.class);
        elasticsearchTemplate.putMapping(Goods.class);

        PageResult<SpuVo> pageResult = spuClient.findSpuByPage("",1,200,2);
        pageResult.getItems().forEach(spuVo -> {
            System.out.println(spuVo.getId());
            try {
                Goods goods = goodService.convert(spuVo);
                goodsRepository.save(goods);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
