package com.leyou.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_spu")
@Data
public class Spu {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private String name;// 商品名称
    private String subTitle;// 副标题，一般是促销信息
    private Long cid1;// 1级类目
    private Long cid2;// 2级类目
    private Long cid3;// 3级类目
    private Long brandId;   //商品所属品牌id
    private Boolean saleable;// 是否上架,0下架，1上架
    private Date createTime;// 添加时间
    private Date updateTime;// 最后修改时间
}