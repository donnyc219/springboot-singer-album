package com.example.demo;


import com.example.demo.helper.DatabaseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PreDestroy;

@SpringBootApplication
public class DemoApplication {


	@Autowired
	DatabaseHelper dbHelper;
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@PreDestroy
	public void cleanupBeforeExit() {
		dbHelper.closeSpanner();
	}

}
