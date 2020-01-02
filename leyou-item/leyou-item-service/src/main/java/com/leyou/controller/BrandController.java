package com.leyou.controller;

import com.leyou.dto.BrandDTO;
import com.leyou.pojo.Brand;
import com.leyou.result.PageResult;
import com.leyou.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2020/1/1 9:00
 * @description:
 */
@RestController
@RequestMapping("/brand")
public class BrandController {


    @Autowired
    private BrandService brandService;


    /**查询品牌数据：分页，排序，模糊查询
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    @GetMapping("/page")
    public ResponseEntity<PageResult<Brand>> queryPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,  //required=false表示这是一个非必要参数
            @RequestParam(value = "desc", required = false) Boolean desc
    ) {
        PageResult<Brand> pageResult = brandService.queryPage(key, page, rows, sortBy, desc);

        return ResponseEntity.ok(pageResult);
    }



    /**
     * 添加品牌
     *
     * @param name
     * @param image
     * @param cids
     * @param letter
     * @return
     */
    @PostMapping
    public ResponseEntity<String> addBrand(
            @RequestParam("name") String name,
            @RequestParam("image") String image,
            @RequestParam("cids") List<Long> cids,
            @RequestParam("letter") Character letter
    ) {

        this.brandService.addBrand(name, image, cids, letter);

        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

    /**
     * 修改品牌
     *
     * @param brand
     * @param ids
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateBrandByCids(BrandDTO brand, @RequestParam("cids") List<Long> ids) {
        brandService.updateBrandByCids(brand, ids);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    /**
     * 删除品牌
     *
     * @param brandId
     * @return
     */
    @DeleteMapping("/deleteBrandById")
    public ResponseEntity<String> deleteBrandById(@RequestParam(value = "id") Long brandId) {
        //先查询一下是否存在中间表关联
        int count = brandService.findBrandAndCategoryByBrandId(brandId);
        if (count > 0) {
            //存在关联,不可删除

            //执意要删除，先删除中间表信息
            brandService.deleteBrandCategoryByBid(brandId);
        }
          //删除品牌Id
        brandService.deleteBrandByBId(brandId);

        return ResponseEntity.ok("删除成功！");
    }


    /**
     * 根据categoryId查询品牌,回显
     *
     * @param cid
     * @return
     */
    @GetMapping("/of/category")
    public ResponseEntity<List<BrandDTO>> findBrandByCategoryById(@RequestParam("id") Long cid) {

        return ResponseEntity.ok(brandService.findBrandByCategoryId(cid));
    }

    /*-------------------根据用户前台页面需求添加方法--------------------------*/


    /**通过Id查询品牌数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<BrandDTO> queryBrandById(@PathVariable("id") Long id){
        return  ResponseEntity.ok(this.brandService.queryBrandById(id));
    }



    /**
     * 根据品牌的id集合查询对应的品牌信息
     *
     * @param ids
     * @return
     */
    @GetMapping("/list")
    ResponseEntity<List<BrandDTO>> queryBrandByIds(@RequestParam("ids") List<Long> ids){
        return ResponseEntity.ok(this.brandService.queryBrandByIds(ids));
    }

}
