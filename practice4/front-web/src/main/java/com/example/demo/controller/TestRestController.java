package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class TestRestController {

    @RequestMapping("/test")
    public String test() {
        return "test";
    }

}
