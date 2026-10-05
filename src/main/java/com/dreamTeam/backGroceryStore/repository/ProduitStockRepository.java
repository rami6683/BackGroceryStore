package com.dreamTeam.backGroceryStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dreamTeam.backGroceryStore.entity.ProduitStock;

public interface ProduitStockRepository extends JpaRepository<ProduitStock, Long> {

}
