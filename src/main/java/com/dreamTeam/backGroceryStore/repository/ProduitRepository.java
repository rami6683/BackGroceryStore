package com.dreamTeam.backGroceryStore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dreamTeam.backGroceryStore.entity.Produit;

public interface ProduitRepository extends JpaRepository<Produit, Long> {

	Optional<Produit> findByCode(String code);

	List<Produit> findByStoreId(Long storeId);

}
