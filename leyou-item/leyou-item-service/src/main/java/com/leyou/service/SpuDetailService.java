package com.leyou.service;

import com.leyou.pojo.SpuDetail;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2020/1/1 17:10
 * @description:
 */
public interface SpuDetailService {

    /**根据Id查询数据
     * @param spuId
     * @return
     */
    SpuDetail querySpuDetailById(long spuId);

    /**根据SpuDetail更新SpuDetail
     * @param spuDetail
     */
    void updateSpuDetailBySpuDetail(SpuDetail spuDetail);
}
