package com.leyou.service;


import com.leyou.dto.BrandDTO;
import com.leyou.pojo.Brand;
import com.leyou.result.PageResult;

import java.awt.*;
import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/17 19:50
 * @description:
 */

public interface BrandService {
    /**
     * 查询品牌数据：分页，排序，模糊查询
     *
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    PageResult<Brand> queryPage(String key, Integer page, Integer rows, String sortBy, Boolean desc);



    /**
     * 添加品牌：记住：品牌分类和品牌之间有中间表关系，虽然数据库里面没有
     * 但是自己应该时刻记住
     *
     * @param name
     * @param image
     * @param cids
     * @param letter
     */
    void addBrand(String name, String image, List<Long> cids, Character letter);




    /**
     * 修改品牌
     *
     * @param brand
     * @param ids
     */
    void updateBrandByCids(BrandDTO brand, List<Long> ids);

    /**
     * 品牌Id查询是否存在中间表信息
     *
     * @param brandId
     * @return
     */
    int findBrandAndCategoryByBrandId(Long brandId);

    /**
     * 根据品牌Id删除中间表信息
     *
     * @param brandId
     */
    void deleteBrandCategoryByBid(Long brandId);

    /**
     * 删除品牌Id
     *
     * @param brandId
     */
    void deleteBrandByBId(Long brandId);


    /**根据品牌Id查询该品牌数据
     * @param brandId
     * @return
     */
    BrandDTO queryBrandByBId(Long brandId);

    /**
     * 根据categoryId查询品牌，回显
     *
     * @param cid
     * @return
     */
    List<BrandDTO> findBrandByCategoryId(Long cid);


    /*-------------------根据用户前台页面需求添加方法--------------------------*/


    /**
     * Id查询品牌
     *
     * @param id
     * @return
     */
    BrandDTO queryBrandById(Long id);


    /**根据ids查询品牌集合
     * @param ids
     * @return
     */
    List<BrandDTO> queryBrandByIds(List<Long> ids);
}
