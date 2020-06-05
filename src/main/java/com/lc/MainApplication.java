package com.lc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
@Author  moni
@Desc    启动类
@Date    2020年5月2日
*/
@SpringBootApplication
@MapperScan("com.lc.dao")
public class MainApplication {
	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

}
