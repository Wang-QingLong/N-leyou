package com.leyou.mapper;

import com.leyou.mapperUtils.BaseMapper;
import com.leyou.pojo.Sku;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/22 17:22
 * @description:
 */
public interface SkuMapper extends BaseMapper<Sku>, SelectByIdListMapper<Sku,Long> {
}
