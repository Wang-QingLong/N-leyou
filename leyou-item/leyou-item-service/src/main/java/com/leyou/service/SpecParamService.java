package com.leyou.service;

import com.leyou.dto.SpecParamDTO;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/21 17:37
 * @description:
 */
public interface SpecParamService {
    /**
     * 根据规格组Id/分类Id查询对应的规格组参数
     *
     * @param gid
     * @return
     */
    List<SpecParamDTO> findParams(Long gid, Long cid,Boolean searching);

}
