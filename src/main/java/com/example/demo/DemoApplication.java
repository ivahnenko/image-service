package com.example.demo;

import com.example.demo.service.AgileEngineImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args)
	{
		ConfigurableApplicationContext ca = SpringApplication.run(DemoApplication.class, args);
		AgileEngineImagesService client = ca.getBean(AgileEngineImagesService.class);

		client.populateLocalCache();
	}

	@Autowired
	private AgileEngineImagesService imagesService;

	@Bean
	public CommandLineRunner runner() {
		return (args) -> imagesService.populateLocalCache();
	}

}

