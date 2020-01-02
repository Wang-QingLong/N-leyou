package com.leyou.dto;


import lombok.Data;

@Data
public class CategoryDTO {
    private Long id;               //类目id
    private String name;          //类目名称
    private Long parentId;       //父类目id,顶级类目填0
    private Boolean isParent;    //是否为父节点，0为否，1为是
    private Integer sort;        //排序指数，越小越靠前
}