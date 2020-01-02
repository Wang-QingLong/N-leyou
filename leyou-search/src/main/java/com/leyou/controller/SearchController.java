package com.leyou.controller;

import com.leyou.dto.SearchRequest;
import com.leyou.pojo.Goods;
import com.leyou.result.PageResult;
import com.leyou.service.GoodsSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class SearchController {

    @Autowired
    private GoodsSearchService goodsSearchService;

    @PostMapping("/page")
    public ResponseEntity<PageResult<Goods>> pageQuery(@RequestBody SearchRequest searchRequest){

        return ResponseEntity.ok(this.goodsSearchService.pageQuery(searchRequest));
    }

    @PostMapping("/filter")
    public ResponseEntity<Map<String, List<?>>>filterQuery(@RequestBody SearchRequest searchRequest){

        return ResponseEntity.ok(this.goodsSearchService.filterQuery(searchRequest));
    }
}
