package com.dreamTeam.backGroceryStore.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dreamTeam.backGroceryStore.dto.StoreRequest;
import com.dreamTeam.backGroceryStore.dto.StoreResponse;
import com.dreamTeam.backGroceryStore.entity.Store;
import com.dreamTeam.backGroceryStore.repository.StoreRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class StoreService {

	private final StoreRepository storeRepository;

	public StoreService(StoreRepository storeRepository) {
		this.storeRepository = storeRepository;
	}

	@Transactional(readOnly = true)
	public List<StoreResponse> findAll() {
		return storeRepository.findAll().stream().map(StoreResponse::from).toList();
	}

	@Transactional(readOnly = true)
	public StoreResponse findById(Long id) {
		return StoreResponse.from(getOrThrow(id));
	}

	@Transactional(readOnly = true)
	public StoreResponse findByCode(String code) {
		return storeRepository.findByCode(code).map(StoreResponse::from)
				.orElseThrow(() -> new EntityNotFoundException("Store introuvable : code=" + code));
	}

	@Transactional
	public StoreResponse create(StoreRequest request) {
		return StoreResponse.from(storeRepository.save(new Store(request.code(), request.description())));
	}

	@Transactional
	public StoreResponse update(Long id, StoreRequest request) {
		Store store = getOrThrow(id);
		store.setCode(request.code());
		store.setDescription(request.description());
		return StoreResponse.from(storeRepository.save(store));
	}

	@Transactional
	public void delete(Long id) {
		storeRepository.delete(getOrThrow(id));
	}

	private Store getOrThrow(Long id) {
		return storeRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Store introuvable : id=" + id));
	}

}
