package com.leyou.service;


import com.leyou.dto.SpuDTO;
import com.leyou.dto.SpuDetailDTO;
import com.leyou.pojo.Spu;
import com.leyou.result.PageResult;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/21 21:57
 * @description: 商品
 */
public interface SpuService {
    /**
     * 查询页面数据
     *
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @return
     */
    PageResult<SpuDTO> querySpus(Integer page, Integer rows, Boolean saleable, String key);


    /**
     * 修改下架商品
     * 根据spuId查询spudetail，回显数据
     *
     * @param spuId
     * @return
     */
    SpuDetailDTO findSpuDetailBySpuId(Long spuId);

    /**
     * 根据spu更新spu数据
     *
     * @param spu
     */
    void updateSpuBySpu(Spu spu);

    /**
     * 更新上架状态
     *
     * @param id
     * @param saleable
     */
    void UpdateSaleable(Long id, boolean saleable);

    /**
     * 根据spuId查询spuDTO
     *
     * @param id
     * @return
     */
    SpuDTO querySpuById(Long id);


    /*-------为了方便将全部商品上架，特意写此方法*/


    /**查询所有的SpuIds
     * @return
     */
    List<Long>  queryAllSpuIds();
}
