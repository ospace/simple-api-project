package com.tistory.ospace.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.tistory.ospace.annotation.timelog.EnableTimeLog;

@EnableTimeLog
@SpringBootApplication
public class Application extends SpringBootServletInitializer {
	private static Class<?> app = Application.class;
	
	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(app);
		springApplication.addListeners(new ApplicationPidFileWriter("app.pid"));
		springApplication.run(args);
	}

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(app);
    }
}
