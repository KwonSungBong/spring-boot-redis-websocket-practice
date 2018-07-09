package com.example.demo.controller;

import com.example.demo.repository.DemoSocketSessionRepository;
import com.example.demo.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

@Controller
public class ClientMessageController {

	private Logger L = LoggerFactory.getLogger(getClass());

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private DemoSocketSessionRepository demoSocketSessionRepository;

//	@MessageMapping("/master/info")
//    @SendToUser("/info/master")
//    public Result masterInfo(String content) {
//		L.info("process masterInfo {}", content);
//        return new Result(content);
//    }

    @MessageMapping("/master/info")
    public void masterInfo(String content, SimpMessageHeaderAccessor headerAccessor) {
        L.info("process masterInfo {}", content);
        sendUesr(headerAccessor.getNativeHeader("userId").get(0), "/info/master", new Result(content));
    }

    private void sendUesr(String userId, String broker, Object data) {
        List<String> users = demoSocketSessionRepository.selectUser(userId);
        for(String user: users) {
            this.template.convertAndSendToUser(user, broker, data);
        }
    }

}
