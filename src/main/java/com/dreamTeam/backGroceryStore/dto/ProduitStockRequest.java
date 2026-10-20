package com.dreamTeam.backGroceryStore.dto;

public record ProduitStockRequest(Long produitId, Long stockId, Integer quantite) {
}
