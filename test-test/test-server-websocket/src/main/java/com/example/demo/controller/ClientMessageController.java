package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class ClientMessageController {

	private Logger L = LoggerFactory.getLogger(getClass());

	@MessageMapping("/master/info")
    @SendToUser("/info/master")
    public Object masterInfo() {
		L.info("process masterInfo");
        return null;
    }

}
