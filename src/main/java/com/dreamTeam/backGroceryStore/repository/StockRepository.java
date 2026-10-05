package com.dreamTeam.backGroceryStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dreamTeam.backGroceryStore.entity.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {

}
