package com.leyou.service;

import com.leyou.clients.ItemFeignClient;
import com.leyou.dto.*;
import com.leyou.enums.ExceptionEnum;
import com.leyou.exception.LyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2020/1/2 12:20
 * @description:
 */
@Slf4j
@Service
public class PageService {

    @Autowired
    private ItemFeignClient itemFeignClient;

    @Autowired
    private SpringTemplateEngine templateEngine;   //模板引擎

    @Value("${ly.static.itemDir}")
    private String itemDir;
    @Value("${ly.static.itemTemplate}")
    private String itemTemplate;


    /**
     * 根据SpuId导入数据
     *
     * @param spuId
     * @return
     */
    public Map<String, Object> loadData(Long spuId) {

        // 查询spu
        SpuDTO spu = itemFeignClient.querySpuById(spuId);
        // 查询分类集合
        List<CategoryDTO> categories = itemFeignClient.queryCategoriesByIds(spu.getCategoryIds());
        // 查询品牌
        BrandDTO brand = itemFeignClient.queryBrandById(spu.getBrandId());
        // 查询规格
        List<SpecGroupDTO> specs = itemFeignClient.querySpecsByCid(spu.getCid3());

        //根据spu的id查询sku集合
        List<SkuDTO> skuDTOS = itemFeignClient.findSkusBySpuId(spuId);

        //根据spuId查询spuDetail
        SpuDetailDTO spuDetailDTO = itemFeignClient.findSpuDetailBySpuId(spuId);
        // 封装数据
        Map<String, Object> data = new HashMap<>();
        data.put("categories", categories);
        data.put("brand", brand);
        data.put("spuName", spu.getName());
        data.put("subTitle", spu.getSubTitle());
        data.put("skus", skuDTOS);
        data.put("detail", spuDetailDTO);
        data.put("specs", specs);
        return data;
    }


    /**
     * 根据spu的id创建html静态页，
     *
     * @param id Context:运行上下文
     */
    public void createItemHtml(Long id) {
        // 上下文，准备模型数据
        Context context = new Context();
        // 调用之前写好的加载数据方法
        context.setVariables(loadData(id));
        // 准备文件路径
        File dir = new File(itemDir);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                // 创建失败，抛出异常
                log.error("【静态页服务】创建静态页目录失败，目录地址：{}", dir.getAbsolutePath());
                throw new LyException(ExceptionEnum.DIRECTORY_WRITER_ERROR);
            }
        }
        File filePath = new File(dir, id + ".html");
        // 准备输出流
        try (PrintWriter writer = new PrintWriter(filePath, "UTF-8")) {
            //模版解析以及最终文件的输出
            templateEngine.process(itemTemplate, context, writer);
        } catch (IOException e) {
            log.error("【静态页服务】静态页生成失败，商品id：{}", id, e);
            throw new LyException(ExceptionEnum.FILE_WRITER_ERROR);
        }
    }

    /**
     * 删除静态页
     *
     * @param id
     */
    public void deleteItemHtml(Long id) {
        File file = new File(itemDir, id + ".html");
        if (file.exists()) {
            if (!file.delete()) {
                log.error("【静态页服务】静态页删除失败，商品id：{}", id);
                throw new LyException(ExceptionEnum.FILE_WRITER_ERROR);
            }
        }
    }
}
