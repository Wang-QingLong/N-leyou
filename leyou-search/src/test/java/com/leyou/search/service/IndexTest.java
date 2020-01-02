package com.leyou.search.service;

import com.leyou.clients.ItemFeignClient;
import com.leyou.dto.SpuDTO;
import com.leyou.pojo.Goods;
import com.leyou.repository.GoodsRepository;
import com.leyou.result.PageResult;
import com.leyou.service.GoodsIndexService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexTest {

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Autowired
    private ItemFeignClient itemFeignClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private GoodsIndexService goodsIndexService;


    /**
     * 创建索引库
     * 关联映射
     */
    @Test
    public void testCreateIndex() {
        esTemplate.createIndex(Goods.class);//创建索引库
        esTemplate.putMapping(Goods.class);//添加映射
    }

    /**
     * 添加数据
     */
    @Test
    public void addData() {

        int page = 1;
        int rows=100;   //每次查询多少条数据

         do{
             PageResult<SpuDTO> spuDTOPageResult = this.itemFeignClient.querySpuByPage(page, rows, true, null);

             //从返回结果中取出spuDTO的集合
             List<SpuDTO> items = spuDTOPageResult.getItems();

             //把spuDTO转换为goods
             List<Goods> goodsList = items.stream()//Stream<SpuDTO>
                     .map(item -> goodsIndexService.buildGoods(item))//Stream<Goods>
                     .collect(Collectors.toList());//List<Goods>

             //批量保存goods对象
             this.goodsRepository.saveAll(goodsList);


             //退出循环条件，当查询条数不满足100时表示已经到达最后一页
             rows = items.size();
             page++;

         }while (rows==100);


        }
    }


