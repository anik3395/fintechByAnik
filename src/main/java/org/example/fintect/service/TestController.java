package org.example.fintect.service;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final RedisTest redisTest;

    @GetMapping("/redis-test")
    public String redisTest() {
        redisTest.test();
        return "Redis test executed!";
    }
}
