package com.dreamTeam.backGroceryStore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dreamTeam.backGroceryStore.entity.Produit;

public interface ProduitRepository extends JpaRepository<Produit, Long> {

}
