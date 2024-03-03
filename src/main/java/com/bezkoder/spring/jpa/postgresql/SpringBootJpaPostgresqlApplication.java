package com.bezkoder.spring.jpa.postgresql;

import com.bezkoder.spring.jpa.postgresql.methods.CustomTask;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@SpringBootApplication
public class SpringBootJpaPostgresqlApplication {

	static CustomTask customTask;
	public static void main(String[] args) {
		SpringApplication.run(SpringBootJpaPostgresqlApplication.class, args);
		customTask=new CustomTask();
	}

	@Scheduled(fixedDelay = 30000,initialDelay = 1000)
	public static void runTask() {
		customTask.run();
	}
}
