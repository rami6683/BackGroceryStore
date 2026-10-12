package com.dreamTeam.backGroceryStore.dto;

public record ProduitRequest(String name, String code, StoreRequest store) {

	public record StoreRequest(String code, String description) {
	}

}
