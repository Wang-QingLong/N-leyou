package com.leyou.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "tb_brand")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Brand {
    @Id
    @KeySql(useGeneratedKeys =true)    //主键自增长伴随回显
    private Long id;
    private String name;// 品牌名称
    private String image;// 品牌图片
    private Character letter; //品牌首字母大写
    private Date createTime;  //创建时间
    private Date updateTime;  //更新时间
}
