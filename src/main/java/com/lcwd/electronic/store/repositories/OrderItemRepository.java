package com.lcwd.electronic.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcwd.electronic.store.entites.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer>
{
}
