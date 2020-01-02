package com.leyou.listeners;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


/**
 * - @Componet：类上的注解，注册到Spring容器
 * - @RabbitListener：方法上的注解，声明这个方法是一个消费者方法，需要指定下面的属性：
 *   - bindings：指定绑定关系，可以有多个。值是@QueueBinding的数组。@QueueBinding包含下面属性：
 *     - value：这个消费者关联的队列。值是@Queue，代表一个队列
 *     - exchange：队列所绑定的交换机，值是@Exchange类型
 *     - key：队列和交换机绑定的RoutingKey
 *
 * 类似listen这样的方法在一个类中可以写多个，就代表多个消费者。
 */
@Component
public class ListenerTest {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "spring.test.queue", durable = "true"),
            exchange = @Exchange(
                    value = "spring.test.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {"#.#"}))
    public void listen(String msg){
        System.out.println("接收到消息：" + msg);
    }
}
