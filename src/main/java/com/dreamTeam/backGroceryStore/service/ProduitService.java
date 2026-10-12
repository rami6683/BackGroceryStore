package com.dreamTeam.backGroceryStore.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dreamTeam.backGroceryStore.dto.ProduitRequest;
import com.dreamTeam.backGroceryStore.entity.Produit;
import com.dreamTeam.backGroceryStore.entity.Store;
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

	// Si un store porte déjà ce code on le réutilise (la description de la requête est ignorée),
	// sinon un nouveau Store est sauvegardé en même temps que le produit grâce au cascade PERSIST.
	@Transactional
	public Produit create(ProduitRequest request) {
		ProduitRequest.StoreRequest storeRequest = request.store();
		Store store = storeRepository.findByCode(storeRequest.code())
				.orElseGet(() -> new Store(storeRequest.code(), storeRequest.description()));

		Produit produit = new Produit(request.name(), request.code());
		produit.setStore(store);
		return produitRepository.save(produit);
	}

}
