package com.dreamTeam.backGroceryStore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dreamTeam.backGroceryStore.entity.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {

	List<Stock> findByStoreId(Long storeId);

}
