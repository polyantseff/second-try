package com.bezkoder.spring.jpa.postgresql;

import com.bezkoder.spring.jpa.postgresql.methods.CustomTask;
import java.util.Calendar;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
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

	@Scheduled(fixedDelay = 30000,initialDelay = 30000)
	public static void runTask() {
		customTask.run();
	}
}
