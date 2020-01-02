package com.leyou.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_spec_param")
@Data
public class SpecParam {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;                 //主键
    private Long cid;                //商品分类Id
    private Long groupId;            //规格组Id
    private String name;              //参数名
    private Boolean numeric;       //是否是数字类型参数，true或false
    private String unit;         //数字类型参数的单位，非数字类型可以为空
    private Boolean generic;     //是否是sku通用属性，true 1或false 0
    private Boolean searching;  //是否用于搜索过滤，true或false
    private String segments;   //数值类型参数，如果需要搜索，
                              // 则添加分段间隔值，如CPU频率间隔：0.5-1.0
    private Date createTime;
    private Date updateTime;
}