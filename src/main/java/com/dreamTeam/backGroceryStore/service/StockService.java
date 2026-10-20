package com.dreamTeam.backGroceryStore.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.dreamTeam.backGroceryStore.dto.StockRequest;
import com.dreamTeam.backGroceryStore.dto.StockResponse;
import com.dreamTeam.backGroceryStore.dto.StoreRequest;
import com.dreamTeam.backGroceryStore.entity.Stock;
import com.dreamTeam.backGroceryStore.entity.Store;
import com.dreamTeam.backGroceryStore.repository.StockRepository;
import com.dreamTeam.backGroceryStore.repository.StoreRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class StockService {

	private final StockRepository stockRepository;

	private final StoreRepository storeRepository;

	public StockService(StockRepository stockRepository, StoreRepository storeRepository) {
		this.stockRepository = stockRepository;
		this.storeRepository = storeRepository;
	}

	@Transactional(readOnly = true)
	public List<StockResponse> findAll() {
		return stockRepository.findAll().stream().map(StockResponse::from).toList();
	}

	@Transactional(readOnly = true)
	public StockResponse findById(Long id) {
		return StockResponse.from(getOrThrow(id));
	}

	@Transactional(readOnly = true)
	public List<StockResponse> findByStore(Long storeId) {
		return stockRepository.findByStoreId(storeId).stream().map(StockResponse::from).toList();
	}

	@Transactional
	public StockResponse create(StockRequest request) {
		Stock stock = new Stock(request.code(), request.description());
		stock.setStore(resolveStore(request.store()));
		return StockResponse.from(stockRepository.save(stock));
	}

	@Transactional
	public StockResponse update(Long id, StockRequest request) {
		Stock stock = getOrThrow(id);
		stock.setCode(request.code());
		stock.setDescription(request.description());
		stock.setStore(resolveStore(request.store()));
		// saveAndFlush : le cascade PERSIST du nouveau store s'exécute au flush, avant de construire la réponse
		return StockResponse.from(stockRepository.saveAndFlush(stock));
	}

	@Transactional
	public void delete(Long id) {
		stockRepository.delete(getOrThrow(id));
	}

	private Stock getOrThrow(Long id) {
		return stockRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Stock introuvable : id=" + id));
	}

	// Même règle que pour Produit : store existant réutilisé par son code, sinon créé grâce au cascade PERSIST.
	private Store resolveStore(StoreRequest request) {
		if (request == null || !StringUtils.hasText(request.code())) {
			throw new IllegalArgumentException("store.code est obligatoire");
		}
		return storeRepository.findByCode(request.code())
				.orElseGet(() -> new Store(request.code(), request.description()));
	}

}
