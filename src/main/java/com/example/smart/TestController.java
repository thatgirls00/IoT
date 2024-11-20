package com.example.smart;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/api/test")
    public String testConnection() {
        System.out.println("됐다잉!");
        return "서버 연결 성공!";
    }
}
