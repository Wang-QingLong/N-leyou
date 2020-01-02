package com.leyou.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.leyou.dto.SpecParamDTO;
import com.leyou.enums.ExceptionEnum;
import com.leyou.exception.LyException;
import com.leyou.mapper.SpecParamMapper;
import com.leyou.pojo.SpecParam;
import com.leyou.service.SpecParamService;
import com.leyou.utils.BeanHelper;
import com.leyou.utils.MySqlExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/21 17:38
 * @description:
 */
@Slf4j
@Service
public class SpecParamServiceImpl implements SpecParamService {

    @Autowired
    private SpecParamMapper specParamMapper;


    /**
     * 根据规格组Id/分类Id查询对应的规格组参数
     *
     * @param gid
     * @param cid
     * @return
     */
    @Override
    public List<SpecParamDTO> findParams(Long gid, Long cid,Boolean searching) {

        //由于通用mapper天生就是一个动态sql，
        // 谁的值不为null谁查，两个都不为null并列查

        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        //
        specParam.setSearching(searching);

        List<SpecParam> specParams = null;
        try {
            //根据Id查询规格组参数
            specParams = specParamMapper.select(specParam);
        } catch (Exception e) {
            log.error("根据Id查询规格组参数异常");
            //判断异常
            MySqlExceptionUtils.CheckMySqlException(e);
        }

        if (CollUtil.isEmpty(specParams)) {
            log.error("根据规格组Id查询参数为空");
            throw new LyException(ExceptionEnum.PARAM_FIND_FILED);
        }

        return BeanHelper.copyWithCollection(specParams, SpecParamDTO.class);
    }
}
