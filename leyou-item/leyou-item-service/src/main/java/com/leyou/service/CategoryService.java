package com.leyou.service;

import com.leyou.dto.CategoryDTO;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/31 13:03
 * @description:
 */
public interface CategoryService {

    /**
     * 根据Pid查询分类
     *
     * @param pid
     * @return
     */
    List<CategoryDTO> queryCategoryByPId(Long pid);

    /**
     * 根据品牌查询商品类目
     *
     * @param brandId
     * @return
     */
    List<CategoryDTO> queryCategorysByBid(Long brandId);



    /**根据cids查询分类集合，以便获取分类名称
     * @param cids
     * @return
     */
    List<CategoryDTO> queryCategoriesBycids(List<Long> cids);



    /*-------------------根据前台页面需求添加方法---------------------------*/


    /**根据Ids查询分类集合
     * @param cids
     * @return
     */
    List<CategoryDTO> queryCategoriesByIds(List<Long> cids);
}
