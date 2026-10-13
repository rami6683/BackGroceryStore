package com.dreamTeam.backGroceryStore.dto;

import com.dreamTeam.backGroceryStore.entity.Produit;

public record ProduitResponse(Long id, String name, String code, String storeCode) {

	public static ProduitResponse from(Produit produit) {
		return new ProduitResponse(produit.getId(), produit.getName(), produit.getCode(), produit.getStore().getCode());
	}

}
