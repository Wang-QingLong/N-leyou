package com.leyou.mapper;

import com.leyou.dto.BrandDTO;
import com.leyou.mapperUtils.BaseMapper;
import com.leyou.pojo.Brand;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/17 19:51
 * @description: 品牌
 */
public interface BrandMapper extends BaseMapper<Brand> {

    /**
     * 往商品和品牌的中间表插入数据
     *
     * @param bid
     * @param cids
     */
    int addBrandCategory(@Param("bid") Long bid, @Param("cids") List<Long> cids);


    /**
     * 删除中间表数据
     *
     * @param id
     */
    void deleteBrandCategoryByBid(@Param("id") Long id);


    /**
     * 根据品牌Id查询是否存在中间表信息
     *
     * @param brandId
     * @return
     */
    int findBrandAndCategory(@Param("id") Long brandId);


    /**
     * 根据categoryId查询品牌，回显
     *
     * @param cid
     * @return
     */
    List<Brand> findBrandByCategoryId(@Param("cid") Long cid);
}
