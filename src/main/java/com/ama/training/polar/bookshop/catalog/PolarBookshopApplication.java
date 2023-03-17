package com.ama.training.polar.bookshop.catalog;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Polar - Bookshop Catalog API", version = "0.0.1"))
public class PolarBookshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(PolarBookshopApplication.class, args);
	}

}
