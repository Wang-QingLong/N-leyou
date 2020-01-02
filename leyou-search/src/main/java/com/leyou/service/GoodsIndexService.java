package com.leyou.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyou.clients.ItemFeignClient;
import com.leyou.dto.*;
import com.leyou.pojo.Goods;
import com.leyou.repository.GoodsRepository;
import com.leyou.utils.BeanHelper;
import com.leyou.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.core.io.NumberInput.parseDouble;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2020/1/2 10:27
 * @description: 创建页面需要的商品信息
 */
@Service
public class GoodsIndexService {


    /*调用服务接口，完成需要的数据需求*/
    @Autowired
    private ItemFeignClient itemFeignClient;

    /*索引库的CRUD使用*/
    @Autowired
    private GoodsRepository goodsRepository;


    //以spu为模板，生成goods
    public Goods buildGoods(SpuDTO spuDTO) {

//转换的时候就已经将spuId，subTitle,brandId,createTime 设置了，因为是共有属性

        Goods goods = BeanHelper.copyProperties(spuDTO, Goods.class);
//设置未添加的属性


        //设置分类id
        goods.setCategoryId(spuDTO.getCid3());

//设置创建时间
        goods.setCreateTime(spuDTO.getCreateTime().getTime());

        /*分类名称拼接*/
        String names = this.itemFeignClient.queryCategoriesByIds(spuDTO.getCategoryIds()).stream()
                .map(CategoryDTO::getName)
                .collect(Collectors.joining(" "));


        String all = new StringBuilder().append(spuDTO.getName()).append(names).toString();

//设置查询数据
        goods.setAll(all);


        List<SkuDTO> skuDTOS = this.itemFeignClient.findSkusBySpuId(spuDTO.getId());

        //用来存放skuMap的集合
        Set<Long> prices = new HashSet<>();

        List<Map<String, Object>> skuMapList = skuDTOS.stream()
                .map(skuDTO -> {
                    Map<String, Object> skuMap = new HashMap<>();
                    skuMap.put("id", skuDTO.getId());
                    skuMap.put("title", skuDTO.getTitle());
                    skuMap.put("price", skuDTO.getPrice());
                    //image的值要从images中获取，然后其多个图片地址以，分隔，使用substringBefore截取第一个，之前的
                    skuMap.put("image", StringUtils.substringBefore(skuDTO.getImages(), ","));

                    //伴随集合遍历，把每个sku的价格存入prices集合中
                    prices.add(skuDTO.getPrice());
                    return skuMap;
                }).collect(Collectors.toList());

//设置skus
        goods.setSkus(JsonUtils.toString(skuMapList));
//设置price
        goods.setPrice(prices);


        //根据分类以及可搜索，查询对应的规格参数
        List<SpecParamDTO> specParamDTOS = this.itemFeignClient.findParams(null, spuDTO.getCid3(), true);


        //获取spuDetail信息，
        SpuDetailDTO spuDetailDTO = this.itemFeignClient.findSpuDetailBySpuId(spuDTO.getId());

        //获取通用规格参数并把其转换为map集合
        //key可搜索规格参数的id，value，可搜索规格参数的值
        Map<Long, Object> genericMap = JsonUtils.nativeRead(spuDetailDTO.getGenericSpec(), new TypeReference<Map<Long, Object>>() {
        });

        //获取特有规格
        //key可搜索规格参数的id，value，可搜索规格参数的可选值数组
        Map<Long, List<String>> specialMap = JsonUtils.nativeRead(spuDetailDTO.getSpecialSpec(), new TypeReference<Map<Long, List<String>>>() {
        });

        Map<String, Object> specs = new HashMap<>();
        //遍历可搜索规格参数的集合，规格参数的名称，作为map的key
        for (SpecParamDTO specParamDTO : specParamDTOS) {
            //可搜索规格参数的id
            Long id = specParamDTO.getId();
            //可搜索规格参数的名称
            String name = specParamDTO.getName();

            Object value = null;
            if (specParamDTO.getGeneric()) { //判断当前可搜索规格参数是通用
                value = genericMap.get(id);//是通用的则从通用规格中取值
            } else {
                value = specialMap.get(id);
            }

            // 判断是否是数值类型,添加单位
            if (specParamDTO.getNumeric()) {
                // 是数字类型，分段
                value = chooseSegment(value, specParamDTO);
            }
            specs.put(name, value);
        }

//设置specs

        goods.setSpecs(specs);

        return goods;
    }


    /**
     * 数字字段分段处理
     *
     * @param value
     * @param p
     * @return
     */
    private String chooseSegment(Object value, SpecParamDTO p) {
        if (value == null || StringUtils.isBlank(value.toString())) {
            return "其它";
        }
        double val = parseDouble(value.toString());
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = parseDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = parseDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }


    /**
     * 根据SpuId创建索引
     *
     * @param spuId
     */
    public void createIndex(Long spuId) {
        SpuDTO spuDTO = itemFeignClient.querySpuById(spuId);

        this.goodsRepository.save(buildGoods(spuDTO));
    }

    /**
     * 根据SpuId删除索引
     *
     * @param spuId
     */
    public void deleteById(Long spuId) {
        this.goodsRepository.deleteById(spuId);
    }
}
