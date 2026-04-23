package org.example.fintect.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisTest {

    private final RedisTemplate redisTemplate;


   public void test(){
//        redisTemplate.opsForValue().set("email","anik@gmail.com");

        Object email = redisTemplate.opsForValue().get("email");

        Object email2 = redisTemplate.opsForValue().get("name");

       System.out.println("Redis Email = " + email);

       System.out.println("Redis Email 2 = " + email2);
    }
}


