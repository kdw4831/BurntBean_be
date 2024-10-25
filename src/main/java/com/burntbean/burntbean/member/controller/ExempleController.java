package com.burntbean.burntbean.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ExempleController {
    @GetMapping("test")
    public String test(){
        return "연결 성공";
    }
}
