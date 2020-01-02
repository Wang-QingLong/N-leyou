package com.leyou.listeners;


import com.leyou.service.GoodsIndexService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.leyou.constants.MQConstants.Exchange.ITEM_EXCHANGE_NAME;
import static com.leyou.constants.MQConstants.Queue.SEARCH_ITEM_DOWN;
import static com.leyou.constants.MQConstants.Queue.SEARCH_ITEM_UP;
import static com.leyou.constants.MQConstants.RoutingKey.ITEM_DOWN_KEY;
import static com.leyou.constants.MQConstants.RoutingKey.ITEM_UP_KEY;

@Component
public class ItemListener {

    @Autowired
    private GoodsIndexService goodsIndexService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = SEARCH_ITEM_UP, durable = "true"),
            exchange = @Exchange(
                    name = ITEM_EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
            key = ITEM_UP_KEY
    ))
    public void listenInsert(Long spuId){
        if(spuId != null){
            // 新增索引
            goodsIndexService.createIndex(spuId);
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = SEARCH_ITEM_DOWN, durable = "true"),
            exchange = @Exchange(
                    name = ITEM_EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
            key = ITEM_DOWN_KEY
    ))
    public void listenDelete(Long spuId){
        if(spuId != null){
            // 删除
            goodsIndexService.deleteById(spuId);
        }
    }
}