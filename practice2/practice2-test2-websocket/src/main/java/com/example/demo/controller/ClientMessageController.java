package com.example.demo.controller;

import com.example.demo.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ClientMessageController {

	private Logger L = LoggerFactory.getLogger(getClass());

    @Autowired
    private SimpMessagingTemplate template;

	@MessageMapping("/master/info")
    @SendTo("/info/master")
    public Result masterInfo(String content) {
		L.info("process masterInfo {}", content);
        return new Result(content);
    }

}
