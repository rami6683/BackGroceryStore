package com.dreamTeam.backGroceryStore.dto;

import com.dreamTeam.backGroceryStore.entity.Store;

public record StoreResponse(Long id, String code, String description) {

	public static StoreResponse from(Store store) {
		return new StoreResponse(store.getId(), store.getCode(), store.getDescription());
	}

}
