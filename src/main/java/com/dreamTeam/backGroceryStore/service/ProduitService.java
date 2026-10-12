package com.dreamTeam.backGroceryStore.service;

import org.springframework.stereotype.Service;

import com.dreamTeam.backGroceryStore.entity.Produit;
import com.dreamTeam.backGroceryStore.repository.ProduitRepository;

@Service
public class ProduitService {

	private final ProduitRepository produitRepository;

	public ProduitService(ProduitRepository produitRepository) {
		this.produitRepository = produitRepository;
	}

	// Le store embarqué dans le produit est créé en même temps grâce au cascade PERSIST.
	public Produit create(Produit produit) {
		return produitRepository.save(produit);
	}

}
