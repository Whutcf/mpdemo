package com.smic.cf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.smic.cf.dao")
public class MpdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MpdemoApplication.class, args);
	}

}
