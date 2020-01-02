package com.leyou.dto;

import lombok.Data;

import java.util.List;


@Data
public class SpecGroupDTO {
    private Long id;

    private Long cid;        //商品分类id，一个分类下有多个规格组

    private String name;     //规格组的名称


    /*----------------------------------------------------*/

    private List<SpecParamDTO> params;
}