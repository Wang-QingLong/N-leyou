package com.leyou.service;

import com.leyou.dto.SkuDTO;
import com.leyou.pojo.Sku;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/22 20:30
 * @description:
 */
public interface SkuService {
    /**
     * 根据spuId查询sku数据，回显
     *
     * @param id
     * @return
     */
    List<SkuDTO> findSkusBySpuId(Long id);

    /**
     * 根据sku查询存在的个数数量
     *
     * @param sku
     * @return
     */
    void deleteSkusBySku(Sku sku);

    /**
     * 插入sku数据集合
     *
     * @param skuList
     */
    void insertSkus(List<Sku> skuList);


    /**
     * 更新sku数据
     *
     * @param saleable
     * @param spuId
     */
    void updateBySku(boolean saleable, Long spuId);
}
