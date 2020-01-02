package com.leyou.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.leyou.dto.SkuDTO;
import com.leyou.dto.SpuDTO;
import com.leyou.enums.ExceptionEnum;
import com.leyou.exception.LyException;
import com.leyou.pojo.Sku;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuDetail;
import com.leyou.service.GoodsService;
import com.leyou.service.SkuService;
import com.leyou.service.SpuDetailService;
import com.leyou.service.SpuService;
import com.leyou.utils.BeanHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/22 17:11
 * @description:
 */
@Slf4j
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private SkuService skuService;

    @Autowired
    private SpuService spuService;

    @Autowired
    private SpuDetailService spuDetailService;


//
//    /**
//     * 添加商品信息
//     *
//     * @param spuDTO
//     */
//    @Override
//    @Transactional
//    public void addGoods(SpuDTO spuDTO) {
//
//
//        //插入spu数据并伴有主键回显
//        Spu spu = BeanHelper.copyProperties(spuDTO, Spu.class);
//
//        try {
//            this.spuMapper.insertSelective(spu);
//        } catch (Exception e) {
//            log.error("spu表数据插入失败");
//            MySqlExceptionUtils.CheckMySqlException(e);
//        }
//
//
//        //获取spuDetail数据
//        SpuDetail spuDetail = BeanHelper.copyProperties(spuDTO.getSpuDetail(), SpuDetail.class);
//        spuDetail.setSpuId(spu.getId());
//        //插入数据
//        try {
//            this.spuDetailMapper.insertSelective(spuDetail);
//        } catch (Exception e) {
//            log.error("spudetail表数据插入");
//            MySqlExceptionUtils.CheckMySqlException(e);
//        }
//        //获取sku数据
//        //挨个插入
//        List<SkuDTO> skus = spuDTO.getSku();
//
//        if (CollUtil.isNotEmpty(skus)) {
//
//            List<Sku> skuList = skus.stream().map(skuDTO -> {
//                skuDTO.setSpuId(spu.getId());
//                return BeanHelper.copyProperties(skuDTO, Sku.class);
//            }).collect(Collectors.toList());
//
//            //插入sku数据
//            try {
//                this.skuMapper.insertList(skuList);
//            } catch (Exception e) {
//                MySqlExceptionUtils.CheckMySqlException(e);
//            }
//        }
//    }


    /**
     * 更新商品信息
     *
     * @param spuDTO
     */
    @Override
    @Transactional
    public void UpdateGoods(SpuDTO spuDTO) {
        Long spuId = spuDTO.getId();
        if (spuId == null) {
            // 请求参数有误
            log.error("请求参数有误:前台传过来的id为空");
            throw new LyException(ExceptionEnum.INVALID_PARAM_ERROR);
        }
        // 1.删除sku
        Sku sku = new Sku();
        sku.setSpuId(spuId);

        skuService.deleteSkusBySku(sku);


        // 2.更新spu
        Spu spu = BeanHelper.copyProperties(spuDTO, Spu.class);
        spu.setSaleable(null);
        spu.setCreateTime(null);
        spu.setUpdateTime(null);

        spuService.updateSpuBySpu(spu);


        // 3.更新spuDetail
        SpuDetail spuDetail = BeanHelper.copyProperties(spuDTO.getSpuDetail(), SpuDetail.class);
        spuDetail.setSpuId(spuId);
        spuDetail.setCreateTime(null);
        spuDetail.setUpdateTime(null);

        spuDetailService.updateSpuDetailBySpuDetail(spuDetail);



        // 4.新增sku
        List<SkuDTO> skuDtos = spuDTO.getSku();

        if (CollUtil.isNotEmpty(skuDtos)) {
            // 处理dto
            List<Sku> skuList = skuDtos.stream().map(dto -> {
                dto.setEnable(false);
                // 添加spu的id
                dto.setSpuId(spuId);
                return BeanHelper.copyProperties(dto, Sku.class);
            }).collect(Collectors.toList());


            skuService.insertSkus(skuList);

        }
    }
}
