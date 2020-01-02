package com.leyou.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.leyou.dto.CategoryDTO;
import com.leyou.enums.ExceptionEnum;
import com.leyou.exception.LyException;
import com.leyou.mapper.CategoryMapper;
import com.leyou.pojo.Category;
import com.leyou.service.CategoryService;
import com.leyou.utils.BeanHelper;
import com.leyou.utils.MySqlExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/31 13:03
 * @description:
 */
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;


    /**
     * 根据Pid查询分类
     *
     * @param pid
     * @return
     */
    @Override
    public List<CategoryDTO> queryCategoryByPId(Long pid) {
        //健壮性判断
        if (pid == null || pid < 0) {
            log.error("前端传入的参数pid不合法");
            throw new LyException(ExceptionEnum.INVALID_PARAM_ERROR);
        }

        Category category = new Category();

        category.setParentId(pid);
        //根据pid查询
        List<Category> categoryList = null;
        try {
            categoryList = categoryMapper.select(category);
        } catch (Exception e) {
            log.error("查询Category表为null");
            MySqlExceptionUtils.CheckMySqlException(e);
        }

        //判断集合是否为空
        if (CollUtil.isEmpty(categoryList)) {
            //查询无值
            throw new LyException(ExceptionEnum.CATEGORY_FINDALL_BE_NULL);
        }


        return BeanHelper.copyWithCollection(categoryList, CategoryDTO.class);
    }


    /**
     *  品牌编辑，先
     *  根据品牌Id查询商品类目
     * 一个品牌可能对应多个商品分类
     *
     * @param brandId
     * @return
     */
    @Override
    public List<CategoryDTO> queryCategorysByBid(Long brandId) {
        //先从中间表根据Bid查询多个分类Ids
        List<Category> list = categoryMapper.queryByBrandId(brandId);
        // 判断结果
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        // 使用自定义工具类，把Category集合转为DTO的集合
        return BeanHelper.copyWithCollection(list, CategoryDTO.class);
    }


    /**根据cids查询分类集合，以便获取分类名称
     * @param cids
     * @return
     */
    @Override
    public List<CategoryDTO> queryCategoriesBycids(List<Long> cids) {
        List<Category> list = null;
        try {
            list = categoryMapper.selectByIdList(cids);
        } catch (Exception e) {
            log.error("queryCategoriesBycids第101行据cids查询分类集合异常");
            MySqlExceptionUtils.CheckMySqlException(e);
        }
        if (CollectionUtils.isEmpty(list)) {
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(list, CategoryDTO.class);
    }

    /*-------------------根据用户前台页面需求添加方法---------------------------*/


    /**
     * 根据Ids查询分类集合
     *
     * @param cids
     * @return
     */
    @Override
    public List<CategoryDTO> queryCategoriesByIds(List<Long> cids) {
        List<Category> list = categoryMapper.selectByIdList(cids);
        if (CollectionUtils.isEmpty(list)) {
            // 没找到，返回404    
            log.error("通过cids查询分类集合失败");
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(list, CategoryDTO.class);
    }


}
