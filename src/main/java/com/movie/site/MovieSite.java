package com.movie.site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.movie.site")
public class MovieSite {

	public static void main(String[] args) {
		SpringApplication.run(MovieSite.class, args);
	}

}
