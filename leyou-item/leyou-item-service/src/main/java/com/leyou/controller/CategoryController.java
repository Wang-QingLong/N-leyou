package com.leyou.controller;

import com.leyou.dto.CategoryDTO;
import com.leyou.service.CategoryService;
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
 * @date: 2019/12/31 11:54
 * @description:
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    /**
     * 根据Pid查询分类
     *
     * @param pid
     * @return
     */
    @GetMapping("/of/parent")
    public ResponseEntity<List<CategoryDTO>> queryCategoryByPId(
            @RequestParam(value = "pid",required = false) Long pid
    ) {
        return ResponseEntity.ok(this.categoryService.queryCategoryByPId(pid));
    }



    /**
     *  品牌编辑，先
     *  根据品牌Id查询商品类目
     * 一个品牌可能对应多个商品分类
     *
     * @param brandId
     * @return
     */
    @GetMapping("/of/brand")
    public ResponseEntity<List<CategoryDTO>> queryByBrandId(@RequestParam(value = "id") Long brandId) {
        return ResponseEntity.ok(this.categoryService.queryCategorysByBid(brandId));
    }


    /*-------------------根据用户前台页面需求添加方法--------------------------*/


    /**
     * 通过Ids查询分类的名称
     *
     * @param ids
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<CategoryDTO>> queryCategoriesByIds(@RequestParam(value = "ids" ,required = false)  List<Long> ids) {
        return ResponseEntity.ok(this.categoryService.queryCategoriesByIds(ids));
    }



}
