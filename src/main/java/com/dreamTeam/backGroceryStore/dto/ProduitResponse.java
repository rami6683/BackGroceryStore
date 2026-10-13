package com.dreamTeam.backGroceryStore.dto;

import com.dreamTeam.backGroceryStore.entity.Produit;
import com.dreamTeam.backGroceryStore.entity.Store;

public record ProduitResponse(Long id, String name, String code, StoreResponse store) {

	public record StoreResponse(Long id, String code, String description) {
	}

	public static ProduitResponse from(Produit produit) {
		Store store = produit.getStore();
		return new ProduitResponse(produit.getId(), produit.getName(), produit.getCode(),
				new StoreResponse(store.getId(), store.getCode(), store.getDescription()));
	}

}
