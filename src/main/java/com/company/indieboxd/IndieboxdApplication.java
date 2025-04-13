package com.company.indieboxd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IndieboxdApplication {

	private static final Logger logger = LoggerFactory.getLogger(IndieboxdApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(IndieboxdApplication.class, args);
		logger.info("Application started");
	}

}
