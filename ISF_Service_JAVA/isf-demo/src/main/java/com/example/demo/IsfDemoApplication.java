package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example"})
@MapperScan("com.example.mapper")
public class IsfDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(IsfDemoApplication.class, args);
	}
}
