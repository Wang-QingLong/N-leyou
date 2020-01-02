package com.leyou;

import com.leyou.service.SpuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @version V1.0
 * @author: WangQingLong
 * @date: 2020/1/2 21:34
 * @description: 将全部商品上架
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
public class UpAll {

    @Autowired
    private SpuService spuService;

    @Test
    public void upAllGoods() {
        List<Long> spuIds = spuService.queryAllSpuIds();
        for (Long spuId : spuIds) {
            this.spuService.UpdateSaleable(spuId, true);
        }
    }
}
