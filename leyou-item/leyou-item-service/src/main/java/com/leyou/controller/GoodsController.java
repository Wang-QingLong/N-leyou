package com.leyou.controller;

import com.leyou.dto.SpuDTO;
import com.leyou.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/22 17:00
 * @description:
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

//
//    /**添加商品，将前台传入的数据封装为对象接收
//     * @param spuDTO
//     * @return
//     */
//    @PostMapping
//    public ResponseEntity<Void> addGoods(@RequestBody SpuDTO spuDTO) {
//
//        this.goodsService.addGoods(spuDTO);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }


    /**更新商品，将前台传入的数据封装为对象接收
     * @param spuDTO
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> UpdateGoods(@RequestBody SpuDTO spuDTO) {

        this.goodsService.UpdateGoods(spuDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
