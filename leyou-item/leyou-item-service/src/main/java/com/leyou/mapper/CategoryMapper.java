package com.leyou.mapper;

import com.leyou.mapperUtils.BaseMapper;
import com.leyou.pojo.Category;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/16 21:12
 * @description:
 */
public interface CategoryMapper extends BaseMapper<Category> {
    /**
     * 根据品牌查询商品类目
     *
     * @param brandId
     * @return
     */
    @Select("SELECT tc.id, tc.`name`, tc.parent_id, tc.is_parent, tc.sort FROM tb_category_brand tcb LEFT JOIN tb_category tc ON tcb.category_id = tc.id WHERE tcb.brand_id = #{id}")
    List<Category> queryByBrandId(@Param("id") Long brandId);
}
