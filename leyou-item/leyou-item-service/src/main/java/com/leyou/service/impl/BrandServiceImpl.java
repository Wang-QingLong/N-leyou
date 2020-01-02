package com.leyou.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.dto.BrandDTO;
import com.leyou.enums.ExceptionEnum;
import com.leyou.exception.LyException;
import com.leyou.mapper.BrandMapper;
import com.leyou.pojo.Brand;
import com.leyou.result.PageResult;
import com.leyou.service.BrandService;
import com.leyou.utils.BeanHelper;
import com.leyou.utils.MySqlExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2019/12/17 19:50
 * @description: 品牌
 */
@Slf4j
@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 查询品牌数据：分页，排序，模糊查询
     *
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    @Override
    public PageResult<Brand> queryPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        //初始化example对象 example相当于是where后面的条件，而criteria是条件容器，可以放置多个对象
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotBlank(key)) {
            //根据name模糊查询，或者根据首字母查询
            criteria.andLike("name", "%" + key + "%")
                    .orEqualTo("letter", key);
        }
        //添加分页条件
        PageHelper.startPage(page, rows);

        //添加排序条件
        if (StringUtils.isNotBlank(sortBy)) {
            example.setOrderByClause(sortBy + " " + (desc ? "desc" : "asc"));
        }
        //通过通用mapper查询
        List<Brand> brands = brandMapper.selectByExample(example);
        //判断是否有值
        if (CollUtil.isEmpty(brands)) {
            log.error("BrandServiceImpl第57行查询品牌数据为空");
            throw new LyException(ExceptionEnum.BRAND_FINDPAGE_BE_NULL);
        }
        //通过分页助手分页
        PageInfo<Brand> brandPageInfo = new PageInfo<>(brands);
//将查询出来的实体类转换成需要返回的DTO结果集
        List<BrandDTO> list = BeanHelper.copyWithCollection(brands, BrandDTO.class);

        return new PageResult(brandPageInfo.getTotal(), brandPageInfo.getPages(), list);
    }


    /**
     * 添加品牌：记住：品牌分类和品牌之间有中间表关系，虽然数据库里面没有
     * 但是自己应该时刻记住
     *
     * @param name
     * @param image
     * @param cids
     * @param letter
     */
    @Override
    @Transactional
    public void addBrand(String name, String image, List<Long> cids, Character letter) {
        //分2步走，第一步新增品牌数据，第二步往中间表添加数据
        //往品牌表brand添加数据


        Brand brand = new Brand();
        //健壮性判断
        if (StringUtils.isNotBlank(name)) {
            brand.setName(name);
        }
        if (StringUtils.isNotBlank(image)) {
            brand.setImage(image);
        }
        if (letter != null) {
            brand.setLetter(letter);
        }
        //插入数据
        int i = brandMapper.insertSelective(brand);
        //如果返回不为1则表示插入失败
        if (i != 1) {
            throw new LyException(ExceptionEnum.BRAND_ADD_FAIL);
        }

        //往中间表插入数据
        int count = brandMapper.addBrandCategory(brand.getId(), cids);

        //如果插入的记录数和实际的cids数量不一致，则报错
        if (count != cids.size()) {
            throw new LyException(ExceptionEnum.BRANDANDCATEGORY_ADD_FAIL);
        }
    }


    /**
     * 修改品牌
     *
     * @param brandDTO
     * @param ids
     */
    @Override
    @Transactional
    public void updateBrandByCids(BrandDTO brandDTO, List<Long> ids) {


        Brand brand = BeanHelper.copyProperties(brandDTO, Brand.class);

        // 修改品牌
        int count = brandMapper.updateByPrimaryKeySelective(brand);
        if (count != 1) {
            // 更新失败，抛出异常
            log.error("updateBrand第141行更新品牌异常");
            throw new LyException(ExceptionEnum.UPDATE_BRAND_FAIL);
        }
        // 先删除旧的中间表数据
        brandMapper.deleteBrandCategoryByBid(brand.getId());

        // 重新插入中间表数据
        count = brandMapper.addBrandCategory(brand.getId(), ids);

        if (count != ids.size()) {
            log.error("updateBrand第151行更新品牌之插入中间表数据异常");
            // 新增失败，抛出异常
            throw new LyException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }

    }

    /**
     * 根据品牌Id查询是否存在中间表信息
     *
     * @param brandId
     * @return
     */
    @Override
    public int findBrandAndCategoryByBrandId(Long brandId) {
        return brandMapper.findBrandAndCategory(brandId);
    }

    /**
     * 根据品牌Id删除中间表信息
     *
     * @param brandId
     */
    @Override
    public void deleteBrandCategoryByBid(Long brandId) {
        brandMapper.deleteBrandCategoryByBid(brandId);

    }

    /**
     * 删除品牌Id
     *
     * @param brandId
     */
    @Override
    public void deleteBrandByBId(Long brandId) {

        brandMapper.deleteByPrimaryKey(brandId);
    }

    /**
     * 根据品牌Id查询品牌数据
     *
     * @param brandId
     * @return
     */
    @Override
    public BrandDTO queryBrandByBId(Long brandId) {
        Brand brand = null;
        try {
            brand = brandMapper.selectByPrimaryKey(brandId);
        } catch (Exception e) {
            log.error("根据id查询出来的品牌数据异常");
            MySqlExceptionUtils.CheckMySqlException(e);
        }
        if (brand == null) {
            log.error("queryBrandByBId第201行根据id查询出来的品牌数据为空");
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }

        return BeanHelper.copyProperties(brand, BrandDTO.class);
    }


    /**
     * 根据categoryId查询品牌，回显
     *
     * @param cid
     * @return
     */
    @Override
    public List<BrandDTO> findBrandByCategoryId(Long cid) {

        List<Brand> brandlist = null;
        try {
            brandlist = brandMapper.findBrandByCategoryId(cid);
        } catch (Exception e) {
            log.error("findBrandByCategoryId根据categoryId查询品牌异常");
            MySqlExceptionUtils.CheckMySqlException(e);
        }

        return BeanHelper.copyWithCollection(brandlist, BrandDTO.class);
    }



    /*-------------------根据用户前台页面需求添加方法--------------------------*/

    /**
     * id查询品牌
     *
     * @param id
     * @return
     */
    @Override
    public BrandDTO queryBrandById(Long id) {
        Brand brand = brandMapper.selectByPrimaryKey(id);
        if (brand == null) {
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }

        return BeanHelper.copyProperties(brand, BrandDTO.class);
    }

    /**
     * 根据品牌Ids集合查询品牌集合为空
     *
     * @param ids
     * @return
     */
    @Override
    public List<BrandDTO> queryBrandByIds(List<Long> ids) {
        List<Brand> brands = this.brandMapper.selectByIdList(ids);

        if (CollectionUtils.isEmpty(brands)) {
            log.error("根据品牌id集合查询品牌数据为空");
            throw new LyException(ExceptionEnum.DATA_NOT_FOUND);
        }
        return BeanHelper.copyWithCollection(brands, BrandDTO.class);
    }
}
