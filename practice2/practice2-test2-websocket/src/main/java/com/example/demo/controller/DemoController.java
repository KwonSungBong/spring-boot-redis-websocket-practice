package com.example.demo.controller;

import com.example.demo.client.DemoMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class DemoController {

    @MessageMapping("/hello")
    @SendTo("/info/demo")
    public DemoMessage demo(String message) {
        return new DemoMessage(message);
    }

}
