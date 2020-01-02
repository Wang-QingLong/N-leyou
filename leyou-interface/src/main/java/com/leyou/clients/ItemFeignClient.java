package com.leyou.clients;

import com.leyou.dto.*;
import com.leyou.result.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient("leyou-item-service")
public interface ItemFeignClient {

    /**
     * 查询SPU信息
     *
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @return
     */
    @GetMapping("spu/page")
    PageResult<SpuDTO> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "key", required = false) String key);


    /**
     * 根据spuId查询spuDTO
     *
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
    SpuDTO querySpuById(@PathVariable("id") Long id);


    /**
     * 根据spuId查询spudetail，
     *
     * @param spuId
     * @return
     */
    @GetMapping("spu/detail")
    SpuDetailDTO findSpuDetailBySpuId(@RequestParam("id") Long spuId);


    /**
     * 根据分类的id集合查询分类集合信息
     *
     * @param ids
     * @return
     */
    @GetMapping("category/list")
    List<CategoryDTO> queryCategoriesByIds(@RequestParam(value = "ids", required = false) List<Long> ids);


    /**
     * 根据spuId查询sku数据
     *
     * @param id
     * @return
     */
    @GetMapping("sku/of/spu")
    List<SkuDTO> findSkusBySpuId(@RequestParam("id") Long id);


    /**
     * 通过Id查询品牌数据
     *
     * @param id
     * @return
     */
    @GetMapping("brand/{id}")
    BrandDTO queryBrandById(@PathVariable("id") Long id);


    /**
     * 根据品牌的id集合查询对应的品牌信息
     *
     * @param ids
     * @return
     */
    @GetMapping("brand/list")
    List<BrandDTO> queryBrandByIds(@RequestParam("ids") List<Long> ids);


    /**
     * 根据规格组Id/分类Id查询对应的规格组参数
     * <p>
     * 用户前台页面搜索条件 加上searching
     *
     * @param gid
     * @return
     */
    @GetMapping("spec/params")
    List<SpecParamDTO> findParams(
            @RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "searching", required = false) Boolean searching
    );


    /**
     * 查询规格参数组，及组内参数
     * @param id 商品分类id
     * @return 规格组及组内参数
     */
    @GetMapping("spec/of/category")
    List<SpecGroupDTO> querySpecsByCid(@RequestParam("id") Long id);
}
