package com.leyou.controller;

import com.leyou.dto.SkuDTO;
import com.leyou.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/22 20:24
 * @description:
 */
@RestController
@RequestMapping("/sku")
public class SkuController {
    @Autowired
    private SkuService skuService;

    /**
     * 根据spuId查询sku数据，回显
     *
     * @param id
     * @return
     */
    @GetMapping("/of/spu")
    public ResponseEntity<List<SkuDTO>> findSkusBySpuId(@RequestParam("id") Long id) {
        return ResponseEntity.ok(this.skuService.findSkusBySpuId(id));
    }


    /**
     * 根据Ids查询Skus数据
     *
     * @param ids
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<SkuDTO>> findSkusByIds(
            @RequestParam("ids") List<Long> ids
    ) {
        return ResponseEntity.ok(this.skuService.findSkusByIds(ids));
    }


}
