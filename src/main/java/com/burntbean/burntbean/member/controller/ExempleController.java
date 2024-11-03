package com.burntbean.burntbean.member.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExempleController {
    @GetMapping("/api/test")  //   localhost:9000/api/test
    public String test(){
        return "연결 성공";
    }
}
