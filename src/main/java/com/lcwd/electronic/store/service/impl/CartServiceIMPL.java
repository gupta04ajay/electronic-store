package com.lcwd.electronic.store.service.impl;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dto.AddItemstoCartRequest;
import com.lcwd.electronic.store.dto.CartDto;
import com.lcwd.electronic.store.entites.Cart;
import com.lcwd.electronic.store.entites.CartItem;
import com.lcwd.electronic.store.entites.Product;
import com.lcwd.electronic.store.entites.User;
import com.lcwd.electronic.store.exceptions.BadApiRequest;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.CartItemRepository;
import com.lcwd.electronic.store.repositories.CartReposetory;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.repositories.UserRepositories;
import com.lcwd.electronic.store.service.CartService;

@Service
public class CartServiceIMPL implements CartService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserRepositories userRepositories;

	@Autowired
	private CartReposetory cartReposetory;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public CartDto addItemToCart(String userId, AddItemstoCartRequest request) {
		String productId = request.getProductId();
		int quantity = request.getQuantity();

		if (quantity <= 0) {
			throw new BadApiRequest("Requested quantity is not valid !!");
		}

		// fetch the product
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found in database"));
		// fetch the user from db
		User user = userRepositories.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User Not found in Databse"));
		Cart cart = null;
		try {
			cart = cartReposetory.findByUser(user).get();
		} catch (NoSuchElementException e) {
			cart = new Cart();
			cart.setCartId(UUID.randomUUID().toString());
			cart.setCreatedAt(new Date());
		}
		// perform cart operations
		// if cart items already present; then update
		AtomicReference<Boolean> updated = new AtomicReference<>(false);
		List<CartItem> items = cart.getItems();
		items = items.stream().map(item -> {

			if (item.getProduct().getProductId().equals(productId)) {
				// item already present in cart
				item.setQuantity(quantity);
				item.setTotalPrice(quantity * product.getDiscountedPrice());
				updated.set(true);
			}
			return item;
		}).collect(Collectors.toList());

//        cart.setItems(updatedItems);

		// create items
		if (!updated.get()) {
			CartItem cartItem = CartItem.builder().quantity(quantity)
					.totalPrice(quantity * product.getDiscountedPrice()).cart(cart).product(product).build();
			cart.getItems().add(cartItem);
		}

		cart.setUser(user);
		Cart updatedCart = cartReposetory.save(cart);
		return mapper.map(updatedCart, CartDto.class);
	}

	@Override
	public void removeItemFromCart(String userId, int cartItem) {
		// conditions
		CartItem cartItem1 = cartItemRepository.findById(cartItem)
				.orElseThrow(() -> new ResourceNotFoundException("Cart Item not found !!"));
		cartItemRepository.delete(cartItem1);
	}

	@Override
	public void clearCart(String userId) {
		// fetch the user from db
		User user = userRepositories.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("user not found in database!!"));
		Cart cart = cartReposetory.findByUser(user)
				.orElseThrow(() -> new ResourceNotFoundException("Cart of given user not found !!"));
		cart.getItems().clear();
		cartReposetory.save(cart);

	}

	@Override
	public CartDto getCartByUser(String userId) {
		User user = userRepositories.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("user not found in database!!"));
		Cart cart = cartReposetory.findByUser(user)
				.orElseThrow(() -> new ResourceNotFoundException("Cart of given user not found !!"));
		return mapper.map(cart, CartDto.class);
	}

}
