package com.dreamTeam.backGroceryStore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.dreamTeam.backGroceryStore.entity.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {

	@RestResource(path = "by-store", rel = "by-store")
	List<Stock> findByStoreId(Long storeId);

}
