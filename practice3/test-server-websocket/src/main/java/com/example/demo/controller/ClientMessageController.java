package com.example.demo.controller;

import com.example.demo.vo.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ClientMessageController {

	private Logger L = LoggerFactory.getLogger(getClass());

	@MessageMapping("/master/info")
    @SendTo("/info/master")
    public Result masterInfo() {
		L.info("process masterInfo");
        return new Result("master");
    }

}
