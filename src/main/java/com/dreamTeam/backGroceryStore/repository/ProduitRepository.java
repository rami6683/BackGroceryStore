package com.dreamTeam.backGroceryStore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.dreamTeam.backGroceryStore.entity.Produit;

public interface ProduitRepository extends JpaRepository<Produit, Long> {

	@RestResource(path = "by-code", rel = "by-code")
	Optional<Produit> findByCode(String code);

}
