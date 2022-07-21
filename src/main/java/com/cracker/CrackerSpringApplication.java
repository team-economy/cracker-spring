package com.cracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;

@EnableJpaAuditing //생성,수정시간 자동업데이트
@SpringBootApplication
public class CrackerSpringApplication {

    @Value("${test.cheoljin}")
    private String test;

    @PostConstruct
    public void test() {
        System.out.println(">>> " + test);
    }
    public static void main(String[] args) {
        SpringApplication.run(CrackerSpringApplication.class, args);
    }

}
