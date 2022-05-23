package com.springbootrabbitmq.topic;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author 成大事
 * @date 2022/1/18 13:27
 */
@Component
public class TopicCustomer {

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,
                    exchange = @Exchange(name = "topics",type = "topic"),
                    key = {"user.save","user.*"}

            )
    })
    public void receive1(String message){
        System.out.println("message 1 : "+message);
    }

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,
                    exchange = @Exchange(name = "topics",type = "topic"),
                    key = {"order.#","produce.*","user.*"}

            )
    })
    public void receive2(String message){
        System.out.println("message 2 : "+message);
    }
}
