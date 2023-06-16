package com.example.NYTtechapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class NytTechAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(NytTechAppApplication.class, args);
    }

}

