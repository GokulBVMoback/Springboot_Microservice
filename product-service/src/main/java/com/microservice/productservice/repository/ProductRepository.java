package com.microservice.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.microservice.productservice.Model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

}
