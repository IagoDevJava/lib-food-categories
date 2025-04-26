package com.egorov.lib_food_categories;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FoodCategoriesApplication {
	public static void main(String[] args) {
		SpringApplication.run(FoodCategoriesApplication.class, args);
	}
}
