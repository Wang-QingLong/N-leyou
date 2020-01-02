package com.leyou.mapperUtils;

import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;


/**
 *如果还需要什么方法可以在后面加相应的接口
 * InsertListMapper有2个，默认是mysql,如果需要换oracle，则选择另外一个
 *
 * @param <T>
 */
@RegisterMapper
public interface BaseMapper<T> extends Mapper<T>, IdListMapper<T, Long>, IdsMapper<T>, InsertListMapper {
}