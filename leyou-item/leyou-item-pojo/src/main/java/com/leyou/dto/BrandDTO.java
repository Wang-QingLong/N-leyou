package com.leyou.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/17 22:07
 * @description: 返回结果中并不包含create_time和update_time字段，所以为了符合页面需求，我们需要给页面量身打造一个用来传递的数据转移对象(Date Transfer Object)
 * 简称DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BrandDTO {

    private Long id;
    private String name;// 品牌名称
    private String image;// 品牌图片
    private Character letter; //品牌首字母大写

}
