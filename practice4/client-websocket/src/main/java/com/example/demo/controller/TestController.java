package com.example.demo.controller;

import com.example.demo.repository.DemoSocketSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private DemoSocketSessionRepository demoSocketSessionRepository;

    @RequestMapping("/count")
    public int count() {
        return demoSocketSessionRepository.getCount();
    }

}
