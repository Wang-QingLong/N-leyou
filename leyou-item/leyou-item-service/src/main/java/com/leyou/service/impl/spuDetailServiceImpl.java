package com.leyou.service.impl;

import com.leyou.enums.ExceptionEnum;
import com.leyou.exception.LyException;
import com.leyou.mapper.SpuDetailMapper;
import com.leyou.pojo.SpuDetail;
import com.leyou.service.SpuDetailService;
import com.leyou.utils.MySqlExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2020/1/1 17:10
 * @description:
 */
@Slf4j
@Service
public class spuDetailServiceImpl implements SpuDetailService {
    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Override
    public SpuDetail querySpuDetailById(long spuId) {
        SpuDetail spuDetail = null;
        try {
            spuDetail = spuDetailMapper.selectByPrimaryKey(spuId);
        } catch (Exception e) {
            log.error("根据spuId查询数据异常");
            MySqlExceptionUtils.CheckMySqlException(e);
        }

        if (spuDetail == null) {
            log.error("根据spuId查询数据为空");
            throw new LyException(ExceptionEnum.DATA_NOT_FILED);
        }

        return spuDetail;
    }


    /**
     * 根据SpuDetail更新SpuDetail
     *
     * @param spuDetail
     */
    @Override
    public void updateSpuDetailBySpuDetail(SpuDetail spuDetail) {
        int count=0;

        try {
            count = spuDetailMapper.updateByPrimaryKeySelective(spuDetail);
        } catch (Exception e) {
            log.error("根据SpuDetail更新SpuDetail异常");
            MySqlExceptionUtils.CheckMySqlException(e);
        }

        if (count != 1) {
            log.error("根据SpuDetail更新SpuDetail失败");
            throw new LyException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }
    }
}
