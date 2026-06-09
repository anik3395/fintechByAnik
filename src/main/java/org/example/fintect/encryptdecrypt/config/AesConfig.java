package org.example.fintect.encryptdecrypt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
public class AesConfig {

    @Bean
    public SecretKey secretKey() {

        String secret = "mZq8kL9p2vX0aR7dE3fG5hJ1kL9mN0==";

        return new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "AES");
    }

}
