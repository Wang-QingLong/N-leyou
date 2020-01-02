package com.leyou.dto;

import lombok.Data;

@Data
public class SkuDTO {
    private Long id;        //sku id
    private Long spuId;     //spu id
    private String title;    //商品标题
    private String images;    //商品的图片，多个图片以‘,’分割
    private Long price;       //销售价格，单位为分
    private Integer stock;   //库存
    private String ownSpec;  // sku的特有规格参数键值对，json格式，
    // 反序列化时请使用linkedHashMap，保证有序
    private String indexes;  // 特有规格属性在spu属性模板中的对应下标组合
    private Boolean enable;// 是否有效，逻辑删除用 :是否有效，0无效，1有效
}