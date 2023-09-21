package com.immpresariat.ArtAgencyApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ArtAgencyAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArtAgencyAppApplication.class, args);
	}

}
