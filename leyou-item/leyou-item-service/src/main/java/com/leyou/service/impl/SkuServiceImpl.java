package com.leyou.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.leyou.dto.SkuDTO;
import com.leyou.enums.ExceptionEnum;
import com.leyou.exception.LyException;
import com.leyou.mapper.SkuMapper;
import com.leyou.pojo.Sku;
import com.leyou.service.SkuService;
import com.leyou.utils.BeanHelper;
import com.leyou.utils.MySqlExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/22 20:30
 * @description:
 */
@Slf4j
@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuMapper skuMapper;

    /**
     * 根据spuId查询sku数据，回显
     *
     * @param spuId
     * @return
     */
    @Override
    public List<SkuDTO> findSkusBySpuId(Long spuId) {

        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spuId", spuId);

        List<Sku> skus = null;
        try {
            skus = this.skuMapper.selectByExample(example);
        } catch (Exception e) {
            log.error("selectByExample执行异常");
            MySqlExceptionUtils.CheckMySqlException(e);
        }
        if (CollUtil.isEmpty(skus)) {
            log.error("selectByExample查询结果为空");
            throw new LyException(ExceptionEnum.FIND_SKUS_BY_SPUID_FAILED);
        }

        return BeanHelper.copyWithCollection(skus, SkuDTO.class);
    }


    /**
     * 根据sku查询存在的个数数量
     *
     * @param sku
     * @return
     */
    @Override
    public void deleteSkusBySku(Sku sku) {
        int i = 0;
        try {
            i = this.skuMapper.selectCount(sku);
        } catch (Exception e) {
            log.error("根据sku查询skuCounts异常");
            MySqlExceptionUtils.CheckMySqlException(e);
        }

        if (i == 0) {
            log.error("根据sku查询skuCounts为空");
            throw new LyException(ExceptionEnum.DATA_NOT_FILED);
        }

        //删除sku

        int y = 0;
        try {
            y = this.skuMapper.delete(sku);
        } catch (Exception e) {
            log.error("根据sku删除sku数据异常");
            MySqlExceptionUtils.CheckMySqlException(e);
        }
        //如果查询到的记录数和删除的记录数不一致则异常
        if (i != y) {
            log.error("更新获取Signature操作失败");
            throw new LyException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }
    }

    /**
     * 插入sku数据集合
     *
     * @param skuList
     */
    @Override
    public void insertSkus(List<Sku> skuList) {
        int count = 0;

        try {
            count = skuMapper.insertList(skuList);
        } catch (Exception e) {
            log.error("插入sku集合数据异常");
            MySqlExceptionUtils.CheckMySqlException(e);
        }

        if (count != skuList.size()) {
            log.error("插入sku集合数据失败");
            throw new LyException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }
    }


    /**
     * 更新sku数据
     *
     * @param saleable
     * @param spuId
     */
    @Override
    public void updateBySku(boolean saleable, Long spuId) {

        Sku sku = new Sku();
        sku.setEnable(saleable);
        //准备更新匹配的条件

        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spuId", spuId);
        //更新

        int i = this.skuMapper.updateByExampleSelective(sku, example);

        int count = this.skuMapper.selectCountByExample(example);

        if (i != count) {
            log.error("更新上架状态失败");
            throw new LyException(ExceptionEnum.UPDATE_SALEABLE_FAIL);
        }
    }


    /**
     * 根据Ids查询Skus数据
     *
     * @param ids
     * @return
     */
    @Override
    public List<SkuDTO> findSkusByIds(List<Long> ids) {

        if (CollUtil.isEmpty(ids)) {
            log.error("前台传过来的SkuIds数据为空");
            throw new LyException(ExceptionEnum.DATA_IS_NULL);
        }

        List<Sku> skus = null;

        try {
            skus = this.skuMapper.selectByIdList(ids);
        } catch (Exception e) {
            log.error("根据SkusIdS查询数据异常");
            MySqlExceptionUtils.CheckMySqlException(e);
        }

        return BeanHelper.copyWithCollection(skus, SkuDTO.class);
    }
}
