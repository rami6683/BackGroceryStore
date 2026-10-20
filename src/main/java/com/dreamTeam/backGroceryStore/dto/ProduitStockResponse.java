package com.dreamTeam.backGroceryStore.dto;

import com.dreamTeam.backGroceryStore.entity.ProduitStock;

public record ProduitStockResponse(Long id, Long produitId, Long stockId, int quantite) {

	public static ProduitStockResponse from(ProduitStock produitStock) {
		return new ProduitStockResponse(produitStock.getId(), produitStock.getProduit().getId(),
				produitStock.getStock().getId(), produitStock.getQuantite());
	}

}
