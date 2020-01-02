package com.leyou.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
public class SpuDTO {
    private Long id;
    private String name;// 商品名称
    private String subTitle;// 副标题，一般是促销信息
    private Long cid1;// 1级类目
    private Long cid2;// 2级类目
    private Long cid3;// 3级类目
    private Long brandId;   //商品所属品牌id
    private Boolean saleable;// 是否上架,0下架，1上架


    private Date createTime;// 添加时间:用于用户前台新品排序使用

/*----------由于业务需求，前端界面需要商品分类和品牌---------------------------*/
    private String categoryName; // 商品分类名称拼接
    private String brandName;// 品牌名称
    /**
     * 由于需要获取商品分类名称拼接，故需要下面这个方法来操作
     * @JsonIgnore注解来忽略这个getCategoryIds方法，避免将其序列化到JSON
     * @return
     */
    @JsonIgnore
    public List<Long> getCategoryIds() {
        return Arrays.asList(cid1, cid2, cid3);
    }
/*------------------------------------------------------------------------*/

//在商品列表更新商品时由于前端传入的数据包含了spuDetail
    private SpuDetailDTO spuDetail;                //spudetail数据
//在商品列表更新商品时由于前端传入的数据包含了skus
    private List<SkuDTO> sku;                     //sku数据




}