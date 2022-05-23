package com.springbootrabbitmq.test;

import com.springbootrabbitmq.SpringbootRabbitMqApplication;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 成大事
 * @date 2022/1/18 12:18
 */
@SpringBootTest(classes = SpringbootRabbitMqApplication.class)
public class TestRabbitMQ {

    //注入rabbitmqTemplate
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //hello world
    @Test
    public void testHelloWorld(){
        rabbitTemplate.convertAndSend("hello","hello world");
    }

    //work
    @Test
    public void testWork(){
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("work","work 模型！");
        }
    }

    //fanout 广播
    @Test
    public void testFanout(){
        rabbitTemplate.convertAndSend("logs","","fanout 模型！");
    }

    // route 路由模式
    @Test
    public void testRoute(){
        rabbitTemplate.convertAndSend("directs","warn","发送info的key 的路由信息！");
    }


    // route 动态路由 订阅模式
    @Test
    public void testTopic(){
        rabbitTemplate.convertAndSend("topics","user.save","user.save 的路由信息！");
    }
}
