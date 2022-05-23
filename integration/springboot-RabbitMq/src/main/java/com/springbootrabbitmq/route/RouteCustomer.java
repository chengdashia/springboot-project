package com.springbootrabbitmq.route;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author 成大事
 * @date 2022/1/18 13:19
 */
@Component
public class RouteCustomer {

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,    //创建临时队列
                    exchange = @Exchange(value = "directs",type = "direct"),    //指定交换机名称和类型
                    key = {"info","error","warn"}
            )
    })
    public void receive1(String message){
        System.out.println("message1 : "+message);
    }


    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,    //创建临时队列
                    exchange = @Exchange(value = "directs",type = "direct"),    //指定交换机名称和类型
                    key = {"error"}
            )
    })
    public void receive2(String message){
        System.out.println("message2 : "+message);
    }
}
