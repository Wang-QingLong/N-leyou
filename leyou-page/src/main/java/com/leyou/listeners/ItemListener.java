package com.leyou.listeners;


import com.leyou.service.PageService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.leyou.constants.MQConstants.Exchange.ITEM_EXCHANGE_NAME;
import static com.leyou.constants.MQConstants.Queue.PAGE_ITEM_DOWN;
import static com.leyou.constants.MQConstants.Queue.PAGE_ITEM_UP;
import static com.leyou.constants.MQConstants.RoutingKey.ITEM_DOWN_KEY;
import static com.leyou.constants.MQConstants.RoutingKey.ITEM_UP_KEY;

@Component
public class ItemListener {

    @Autowired
    private PageService pageService;

    //一个监听器只能监听一个队列
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = PAGE_ITEM_UP, durable = "true"),
            exchange = @Exchange(
                    name = ITEM_EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
            key = ITEM_UP_KEY
    ))
    public void listenInsert(Long id) {
        if (id != null) {
            // 新增或修改
            pageService.createItemHtml(id);
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = PAGE_ITEM_DOWN, durable = "true"),
            exchange = @Exchange(
                    name = ITEM_EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
            key = ITEM_DOWN_KEY
    ))

    public void listenDelete(Long id) {
        if (id != null) {
            // 删除
            pageService.deleteItemHtml(id);
        }
    }
}