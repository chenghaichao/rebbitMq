package com.example.testrabbitmqconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Value 获取配置文件值
 */
@RestController
@RequestMapping("test")
public class Test {
    @Autowired
    private DurableSender durableSender;
    @Value("${test.t}")
    public String houst;

    @GetMapping("/a")
    public String get() {

        return houst;
    }

    @GetMapping("/send")
    public void send() throws InterruptedException {

//
//        int flag = 0;
//        while (true) {
//            flag++;
//
//            Thread.sleep(2000);
//            durableSender.send("hello--" + flag);
//        }

    }
}
