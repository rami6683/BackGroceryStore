package com.dreamTeam.backGroceryStore.dto;

import com.dreamTeam.backGroceryStore.entity.Stock;

public record StockResponse(Long id, String code, String description, StoreResponse store) {

	public static StockResponse from(Stock stock) {
		return new StockResponse(stock.getId(), stock.getCode(), stock.getDescription(),
				StoreResponse.from(stock.getStore()));
	}

}
