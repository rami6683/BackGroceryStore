package com.dreamTeam.backGroceryStore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dreamTeam.backGroceryStore.entity.ProduitStock;

public interface ProduitStockRepository extends JpaRepository<ProduitStock, Long> {

	List<ProduitStock> findByProduitId(Long produitId);

}
