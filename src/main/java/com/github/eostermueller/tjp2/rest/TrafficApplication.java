package com.github.eostermueller.tjp2.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;

@SpringBootApplication(exclude = {JacksonAutoConfiguration.class})
public class TrafficApplication {
	public static void main(String[] args) {
		SpringApplication.run(TrafficApplication.class, args);
    }       
}            