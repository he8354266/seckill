package com.example.project.controller;

import com.example.project.Service.UserService;
import com.example.project.rabbitmq.MQSender;
import com.example.project.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Title: project
 * @Package * @Description:     * @author CodingSir
 * @date 2020/9/815:59
 */
@Controller
@RequestMapping("/demo")
public class SampleController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MQSender sender;
    @RequestMapping("/mq/header")
    @ResponseBody
    public Result<String> header() {
        sender.sendTopic("hello imooc");
        return Result.success("hello world");
    }
    @RequestMapping("/mq/fanout")
    @ResponseBody
    public Result<String> fanout() {
        sender.sendTopic("hello imooc");
        return Result.success("hello world");
    }
    @RequestMapping("/mq/topic")
    @ResponseBody
    public Result<String> topic() {
        sender.sendTopic("hello imooc");
        return Result.success("hello world");
    }
    @RequestMapping("/mq")
    @ResponseBody
    public Result<String> mq(){
        sender.send("hello,imooc");
        return Result.success("hello world");
    }
}
