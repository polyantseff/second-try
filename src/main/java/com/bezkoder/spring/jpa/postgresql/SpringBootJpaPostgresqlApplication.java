package com.bezkoder.spring.jpa.postgresql;

import com.bezkoder.spring.jpa.postgresql.methods.CustomTask;
import java.util.Calendar;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;

@SpringBootApplication
public class SpringBootJpaPostgresqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootJpaPostgresqlApplication.class, args);
		runTask();
	}

	public static void runTask() {
		Calendar calendar = Calendar.getInstance();
		Timer time = new Timer(); // Instantiate Timer Object
		time.schedule(new CustomTask(), calendar.getTime(), TimeUnit.SECONDS.toMillis(30));
	}
}
