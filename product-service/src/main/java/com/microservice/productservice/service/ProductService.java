package com.microservice.productservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.microservice.productservice.Model.Product;
import com.microservice.productservice.dto.ProductRequest;
import com.microservice.productservice.dto.ProductResponse;
import com.microservice.productservice.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor //it will create required constructor
@Slf4j //for add log
public class ProductService {
	
	private final ProductRepository productRepository;
	
	
	public void createProduct(ProductRequest productRequest)
	{	
		Product	product=Product.builder()
				.name(productRequest.getName())
				.description(productRequest.getDescription())
				.price(productRequest.getPrice())
				.build();
		productRepository.save(product);
		log.info("Product {} is saved", product.getId()); 
		//instead of concatenate string ("Product "+product.getId()+" is saved"), we can use {}(placeholder) by @slf4j
	}
	
	public List<ProductResponse> getAllProducts()
	{
		List<Product> products=productRepository.findAll();
		return products.stream().map(this::mapToProductResponse).toList();
		//instead of Lambda expression(return products.stream().map(product -> mapToProductResponse(product)).toList();) we are using method reference
	}
	
	private ProductResponse mapToProductResponse(Product product)
	{
		return ProductResponse.builder()
				.id(product.getId())
				.name(product.getName())
				.description(product.getDescription())
				.price(product.getPrice())
						.build();
	}

}
