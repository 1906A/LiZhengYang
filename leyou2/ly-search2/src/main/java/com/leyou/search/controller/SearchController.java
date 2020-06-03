package com.leyou.search.controller;

import com.leyou.common.PageResult;
import com.leyou.pojo.Brand;
import com.leyou.pojo.Category;
import com.leyou.pojo.SpecParam;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.SpecClient;
import com.leyou.search.item.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.repository.GoodsRepository;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("search")
public class SearchController {

    @Autowired
    GoodsRepository goodsRepository;
    @Autowired
    CategoryClient categoryClient;
    @Autowired
    BrandClient brandClient;
    @Autowired
    SpecClient specClient;

    @RequestMapping("page")
    public PageResult<Goods> page(@RequestBody SearchRequest searchRequest){

        System.out.println(searchRequest.getKey()+"===="+searchRequest.getPage());

        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();

        builder.withQuery(QueryBuilders.matchQuery("all",searchRequest.getKey()).operator(Operator.AND));

        builder.withPageable(PageRequest.of(searchRequest.getPage() - 1, searchRequest.getSize()));

        builder.withSort(SortBuilders.fieldSort(searchRequest.getSortBy())
                .order(searchRequest.getDescending()? SortOrder.DESC:SortOrder.ASC));

        String categoryName = "categoryName";
        String brandName = "brandName";

        builder.addAggregation(AggregationBuilders.terms(categoryName).field("cid3"));
        builder.addAggregation(AggregationBuilders.terms(brandName).field("brandId"));

        AggregatedPage<Goods> search = (AggregatedPage<Goods>)goodsRepository.search(builder.build());

        LongTerms categoryAgg = (LongTerms)search.getAggregation(categoryName);
        List<Category> categoryList = new ArrayList<>();
        categoryAgg.getBuckets().forEach(bucket -> {
            Long categoryId = (Long)bucket.getKey();
            Category category = categoryClient.findCategoryById(categoryId);
            categoryList.add(category);
        });

        LongTerms brandAgg = (LongTerms)search.getAggregation(brandName);
        List<Brand> brandList = new ArrayList<>();
        brandAgg.getBuckets().forEach(bucket -> {
            Long brandId =(Long)bucket.getKey();

            Brand brand = brandClient.findBrandById(brandId);
            brandList.add(brand);
        });

        List<Map<String,Object>> paramList = new ArrayList<>();
        if (categoryList.size()==1){
            List<SpecParam> specParams = specClient.findSpecParamByCidAndSearching(categoryList.get(0).getId());
            specParams.forEach(specParam -> {
                String key = specParam.getName();
                builder.addAggregation(AggregationBuilders.terms(key).field("specs."+key+".keyword"));
            });

            AggregatedPage<Goods> search1 = (AggregatedPage<Goods>)goodsRepository.search(builder.build());
            Map<String, Aggregation> aggregationMap = search1.getAggregations().asMap();

            aggregationMap.keySet().forEach(mKey ->{
                if (!(mKey.equals(categoryName)||mKey.equals(brandName))){
                    StringTerms aggregation = (StringTerms)aggregationMap.get(mKey);
                    Map<String,Object> map = new HashMap<>();
                    map.put("key",mKey);
                    List<Map<String,String>> list = new ArrayList<>();

                    aggregation.getBuckets().forEach(bucket -> {
                        Map<String,String> valueMap = new HashMap<>();
                        valueMap.put("name",bucket.getKeyAsString());
                        list.add(valueMap);
                    });

                    map.put("options",list);
                    paramList.add(map);
                }
            });
        }



        //Page<Goods> search = goodsRepository.search(builder.build());

        return new SearchResult(search.getTotalElements(), search.getContent(),
                search.getTotalPages(),categoryList,brandList,paramList);
    }
}
