package com.leyou.controller;

import com.leyou.dto.SpuDTO;
import com.leyou.dto.SpuDetailDTO;
import com.leyou.result.PageResult;
import com.leyou.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/21 21:10
 * @description:
 */
@RestController
@RequestMapping("/spu")
public class SpuController {
    @Autowired
    private SpuService spuService;


    /**
     * 商品列表查询
     *
     *
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @return
     */
    @GetMapping("/page")
    public ResponseEntity<PageResult<SpuDTO>> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key) {
        return ResponseEntity.ok(spuService.querySpus(page, rows, saleable, key));
    }




    /**
     * 修改下架商品
     * 根据spuId查询spudetail，
     *
     * @param spuId
     * @return
     */
    @GetMapping("/detail")
    public ResponseEntity<SpuDetailDTO> findSpuDetailBySpuId(@RequestParam("id") Long spuId) {

        return ResponseEntity.ok(this.spuService.findSpuDetailBySpuId(spuId));
    }


    /**
     * 更新上架状态
     *
     * @param id
     * @param saleable
     * @return
     */
    @PutMapping("/saleable")
    public ResponseEntity<Void> UpdateSaleable(
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "saleable") boolean saleable
    ) {

        this.spuService.UpdateSaleable(id, saleable);

        return ResponseEntity.ok().build();
    }

/*--------------------------Page模块需要---------------------------*/
    /**
     * 根据spuId查询spuDTO
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<SpuDTO> querySpuById(@PathVariable("id")Long id){

        return ResponseEntity.ok(this.spuService.querySpuById(id));
    }






}


