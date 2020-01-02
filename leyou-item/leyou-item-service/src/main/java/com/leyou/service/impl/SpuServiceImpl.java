package com.leyou.service.impl;


import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.constants.MQConstants;
import com.leyou.dto.CategoryDTO;
import com.leyou.dto.SpuDTO;
import com.leyou.dto.SpuDetailDTO;
import com.leyou.enums.ExceptionEnum;
import com.leyou.exception.LyException;
import com.leyou.mapper.SpuMapper;
import com.leyou.pojo.Sku;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuDetail;
import com.leyou.result.PageResult;
import com.leyou.service.*;
import com.leyou.utils.BeanHelper;
import com.leyou.utils.MySqlExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.stream.Collectors;

import static com.leyou.constants.MQConstants.Exchange.ITEM_EXCHANGE_NAME;
import static com.leyou.constants.MQConstants.RoutingKey.ITEM_DOWN_KEY;
import static com.leyou.constants.MQConstants.RoutingKey.ITEM_UP_KEY;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/21 21:57
 * @description:
 */
@Slf4j
@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private SpuDetailService spuDetailService;

    @Autowired
    private SkuService skuService;


    /**
     * 商品列表查询
     *
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @return
     */
    @Override
    public PageResult<SpuDTO> querySpus(Integer page, Integer rows, Boolean saleable, String key) {
        // 1 分页
        PageHelper.startPage(page, rows);
        // 2,获取动态sql生成器
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        // 2.1 搜索条件过滤
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("name", "%" + key + "%");
        }
        // 2.2 上下架过滤
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }
        // 2.3 默认按时间排序
//        example.setOrderByClause("update_time DESC");
        // 3 查询结果
        List<Spu> list = null;
        try {
            list = spuMapper.selectByExample(example);
        } catch (Exception e) {
            MySqlExceptionUtils.CheckMySqlException(e);
        }
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.GOODS_NOT_FOUND);
        }
        // 4 封装分页结果
        PageInfo<Spu> info = new PageInfo<>(list);

        // DTO转换
        List<SpuDTO> spuDTOList = BeanHelper.copyWithCollection(list, SpuDTO.class);

        //迭代分类集合，挨个取出name,并把所有的name,拼接为字符串，分隔符自定义

        spuDTOList.forEach(spuDTO -> {
            //跨业务根据Id查询分类
            List<CategoryDTO> categoryDTOS = this.categoryService.queryCategoriesBycids(spuDTO.getCategoryIds());

            String names = categoryDTOS.stream()     //List<CategoryDTO  ==> Stream<CategoryDTO> >
                    .map(CategoryDTO::getName)
                    .collect(Collectors.joining("/"));

            spuDTO.setCategoryName(names);

            spuDTO.setBrandName(this.brandService.queryBrandByBId(spuDTO.getBrandId()).getName());

        });

        return new PageResult<SpuDTO>(info.getTotal(), info.getPages(), spuDTOList);
    }


    /**
     * 修改下架商品
     * 根据spuId查询spudetail，回显数据
     *
     * @param spuId
     * @return
     */
    @Override
    public SpuDetailDTO findSpuDetailBySpuId(Long spuId) {

        return BeanHelper.copyProperties(spuDetailService.querySpuDetailById(spuId), SpuDetailDTO.class);
    }


    /**
     * 根据spu更新spu数据
     *
     * @param spu
     */
    @Override
    public void updateSpuBySpu(Spu spu) {

        int count = 0;

        try {
            count = spuMapper.updateByPrimaryKeySelective(spu);
        } catch (Exception e) {
            log.error("根据spu更新spu数据异常");
            MySqlExceptionUtils.CheckMySqlException(e);
        }
        if (count != 1) {
            log.error("根据spu更新spu数据失败");
            throw new LyException(ExceptionEnum.UPDATE_OPERATION_FAIL);
        }
    }

    @Autowired
    private AmqpTemplate amqpTemplate;


    /**
     * 更新上架状态
     * 注意:在修改spu属性的同时，还需要修改
     * 因为spu下架，sku也要跟着进行下架
     * <p>
     * 给商品上下架添加消息队列
     *
     * @param spuId
     * @param saleable
     */
    @Override
    @Transactional
    public void UpdateSaleable(Long spuId, boolean saleable) {


        //我们在修改spu属性的同时，还需要修改sku的enable属性，
        // 因为spu下架，sku也要跟着进行下架
        Spu spu = new Spu();
        spu.setSaleable(saleable);
        spu.setId(spuId);

        //更新spu数据
        try {
            this.spuMapper.updateByPrimaryKeySelective(spu);
        } catch (Exception e) {
            log.error("更新上架状态失败");
            //如果出错则抛异常
            MySqlExceptionUtils.CheckMySqlException(e);
        }

        //更新sku

        skuService.updateBySku(saleable, spuId);


        String key = saleable ? ITEM_UP_KEY : ITEM_DOWN_KEY;

        //商品服务负责消息的发出，routingkey不同，所以其实可以把消息发给不同队列
        this.amqpTemplate.convertAndSend(ITEM_EXCHANGE_NAME, key, spuId);

    }




    /*--------------------------Page模块需要---------------------------*/

    /**
     * 根据spuId查询spuDTO
     *
     * @param id
     * @return
     */
    @Override
    public SpuDTO querySpuById(Long id) {

        Spu spu = this.spuMapper.selectByPrimaryKey(id);
        if (null == spu) {
            log.error("根据id查询Spu数据异常");
            throw new LyException(ExceptionEnum.DATA_NOT_FOUND);
        }
        return BeanHelper.copyProperties(spu, SpuDTO.class);
    }


    /*-------为了方便将全部商品上架，特意写此方法*/


    /**
     * 查询所有的Ids
     *
     * @return
     */
    @Override
    public List<Long> queryAllSpuIds() {

        List<Spu> spus = spuMapper.selectAll();

        if (CollUtil.isEmpty(spus)) {
            log.error("查询所有SpuIds失败");
            throw new LyException(ExceptionEnum.DATA_NOT_FILED);
        }

        return spus.stream().map(Spu::getId
        ).collect(Collectors.toList());
    }
}
