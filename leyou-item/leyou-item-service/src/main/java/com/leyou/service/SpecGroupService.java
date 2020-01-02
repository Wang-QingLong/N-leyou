package com.leyou.service;

import com.leyou.dto.SpecGroupDTO;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/21 16:01
 * @description:
 */
public interface SpecGroupService {
    /**
     * 根据categoryId查询商品规格组信息
     *
     * @param cid
     * @return
     */
    List<SpecGroupDTO> querySpecGroupsByCid(Long cid);



    /*------------前台业务需求------------------*/


    /**根据Id查询规则参数
     * @param id
     * @return
     */
    List<SpecGroupDTO> querySpecsByCid(Long id);
}
