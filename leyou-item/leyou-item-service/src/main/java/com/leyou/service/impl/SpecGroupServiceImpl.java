package com.leyou.service.impl;

import com.leyou.dto.SpecGroupDTO;
import com.leyou.dto.SpecParamDTO;
import com.leyou.enums.ExceptionEnum;
import com.leyou.exception.LyException;
import com.leyou.mapper.SpecGroupServiceMapper;
import com.leyou.pojo.SpecGroup;
import com.leyou.service.SpecGroupService;
import com.leyou.service.SpecParamService;
import com.leyou.utils.BeanHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/21 16:01
 * @description:
 */
@Slf4j
@Service
public class SpecGroupServiceImpl implements SpecGroupService {

    @Autowired
    private SpecGroupServiceMapper specGroupServiceMapper;

    @Autowired
    private SpecParamService specParamService;

    /**
     * 根据cid查询规格参数组信息
     *
     * @param cid
     * @return
     */
    @Override
    public List<SpecGroupDTO> querySpecGroupsByCid(Long cid) {

        // 查询规格组
        SpecGroup s = new SpecGroup();
        s.setCid(cid);
        List<SpecGroup> list = null;
        try {
            list = specGroupServiceMapper.select(s);
        } catch (Exception e) {
            log.error("querySpecGroupsByCid第45行查询规格组异常");
            //如果产生异常，则表明是数据库连接异常或者表字段对应不上
            throw new LyException(ExceptionEnum.BRAND_SELECT_FAIL);
        }

        //判断查询结果是否为空
        if (CollectionUtils.isEmpty(list)) {
            log.error("商品规格组查询为空");
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }

        // 对象转换
        return BeanHelper.copyWithCollection(list, SpecGroupDTO.class);
    }



    /*-----------------------------------------------------------*/
    /**
     * 根据Id查询规则参数
     *
     * @param id
     * @return
     */
    @Override
    public List<SpecGroupDTO> querySpecsByCid(Long id) {
        // 查询规格组
        List<SpecGroupDTO> groupList = querySpecGroupsByCid(id);

        // 查询分类下所有规格参数
        List<SpecParamDTO> params = specParamService.findParams(null, id, null);


        // 将规格参数按照groupId进行分组，得到每个group下的param的集合   key,groupId,value,当前组id对应的规格参数
        Map<Long, List<SpecParamDTO>> paramMap = params.stream()
                .collect(Collectors.groupingBy(SpecParamDTO::getGroupId));


        // 填写到group中
        for (SpecGroupDTO groupDTO : groupList) {
            //根据组id取值
            groupDTO.setParams(paramMap.get(groupDTO.getId()));
        }
        return groupList;
    }


}
