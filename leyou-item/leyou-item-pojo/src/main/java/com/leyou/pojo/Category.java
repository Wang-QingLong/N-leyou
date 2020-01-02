package com.leyou.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/16 21:15
 * @description:
 */
@Data
@Table(name = "tb_category")
public class Category {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;               //类目id
    private String name;          //类目名称
    private Long parentId;       //父类目id,顶级类目填0
    private Boolean isParent;    //是否为父节点，0为否，1为是
    private Integer sort;        //排序指数，越小越靠前
    private Date createTime;   //数据创建时间
    private Date updateTime;    //数据更新时间
}
