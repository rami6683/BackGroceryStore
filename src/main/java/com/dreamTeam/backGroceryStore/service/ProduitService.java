package com.dreamTeam.backGroceryStore.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dreamTeam.backGroceryStore.entity.Produit;
import com.dreamTeam.backGroceryStore.repository.ProduitRepository;
import com.dreamTeam.backGroceryStore.repository.StoreRepository;

@Service
public class ProduitService {

	private final ProduitRepository produitRepository;

	private final StoreRepository storeRepository;

	public ProduitService(ProduitRepository produitRepository, StoreRepository storeRepository) {
		this.produitRepository = produitRepository;
		this.storeRepository = storeRepository;
	}

	// Si un store porte déjà ce code on le réutilise (la description du JSON est ignorée),
	// sinon le store embarqué est créé en même temps grâce au cascade PERSIST.
	@Transactional
	public Produit create(Produit produit) {
		if (produit.getStore() != null && produit.getStore().getCode() != null) {
			storeRepository.findByCode(produit.getStore().getCode()).ifPresent(produit::setStore);
		}
		return produitRepository.save(produit);
	}

}
