package com.lcwd.electronic.store.service;

import com.lcwd.electronic.store.dto.AddItemstoCartRequest;
import com.lcwd.electronic.store.dto.CartDto;

public interface CartService {
	// add items to cart
	//case1: cart for user is not available : we will create the cart and add the items
	//case2: cart available add the items to cart
	
	CartDto addItemToCart(String userId,AddItemstoCartRequest request);
	
	//remove item from cart
	void removeItemFromCart(String userId,int cartItem);
	
	//remove all items from cart
    void clearCart(String userId);
    
    CartDto getCartByUser(String userId);

}
