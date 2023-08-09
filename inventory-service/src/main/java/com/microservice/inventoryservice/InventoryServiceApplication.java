package com.microservice.inventoryservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;


import com.microservice.inventoryservice.repository.InventoryRepository;
import com.microservice.inventoryservice.model.Inventory;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner LoadData(InventoryRepository inventoryRepo)
	{
		return args -> {
			Inventory inventory=new Inventory();
			inventory.setSkuCode("Mango");
			inventory.setQuantity(100);
			
			Inventory inventory2=new Inventory();
			inventory2.setSkuCode("Banana");
			inventory2.setQuantity(0);
			
			inventoryRepo.save(inventory);
			inventoryRepo.save(inventory2);
		};
		
	}
}
