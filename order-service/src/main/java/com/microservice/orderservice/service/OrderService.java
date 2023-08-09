package com.microservice.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.microservice.orderservice.dto.InventoryResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.microservice.orderservice.dto.OrderLineItemsDto;
import com.microservice.orderservice.dto.OrderRequest;
import com.microservice.orderservice.model.Order;
import com.microservice.orderservice.model.OrderLineItems;
import com.microservice.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
	
	private final OrderRepository orderRepository;
	private final WebClient.Builder webClientBuilder;
	
	public void placeOrder(OrderRequest orderRequest)
	{
		Order order=new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		
		List<OrderLineItems> orderLineItems=orderRequest.getOrderLineItemsDtoList()
		.stream()
		.map(this::mapToDto).toList();

		order.setOrderLineItemsList(orderLineItems);

		List<String> skuCodes=order.getOrderLineItemsList().stream()
				.map(OrderLineItems::getSkuCode).toList();

		InventoryResponse[] inventoryResponses=webClientBuilder.build().get()
				.uri("http://inventory-service/api/inventory",
				uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
						.retrieve()
								.bodyToMono(InventoryResponse[].class)
										.block();

		//Mono means or is a datatype in reactive framework
			/* by default WebClient will make Asynchronous request,
			but we have to make a Synchronous request, so we use ".block()" */

		Boolean allProductsInStock= Arrays.stream(inventoryResponses)
				.allMatch(InventoryResponse::isInStock);

		if(allProductsInStock)
		{
			orderRepository.save(order);
		}
		else {
			throw new IllegalArgumentException("Product is not in stock, please try again later");
		}

	}

	private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
		OrderLineItems orderLineItems=new OrderLineItems();
		orderLineItems.setPrice(orderLineItemsDto.getPrice());
		orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
		orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
		return orderLineItems;
	}

}
