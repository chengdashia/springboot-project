package com.springbootrabbitmq.hello;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author 成大事
 * @date 2022/1/18 12:53
 */
@Component   //持久化  非独占  不是自动删除
@RabbitListener(queuesToDeclare = @Queue(value = "hello",durable = "false",autoDelete = "true"))
public class helloCustomer {

    @RabbitHandler
    public void receivel(String message){
        System.out.println("message = "+message);
    }
}

