package com.dna.sourcing;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// @MapperScan(basePackages = "com.dna.sourcing.mapper")
@tk.mybatis.spring.annotation.MapperScan("com.dna.sourcing.mapper")
public class DNASourcingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DNASourcingApplication.class, args);
    }

}
