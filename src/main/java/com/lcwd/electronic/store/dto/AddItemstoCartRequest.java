package com.lcwd.electronic.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddItemstoCartRequest {

	private String productId;
	private int quantity;
	
}
