package com.dreamTeam.backGroceryStore.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.dreamTeam.backGroceryStore.dto.ProduitRequest;
import com.dreamTeam.backGroceryStore.dto.ProduitResponse;
import com.dreamTeam.backGroceryStore.dto.StoreRequest;
import com.dreamTeam.backGroceryStore.entity.Produit;
import com.dreamTeam.backGroceryStore.entity.Store;
import com.dreamTeam.backGroceryStore.repository.ProduitRepository;
import com.dreamTeam.backGroceryStore.repository.StoreRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProduitService {

	private final ProduitRepository produitRepository;

	private final StoreRepository storeRepository;

	public ProduitService(ProduitRepository produitRepository, StoreRepository storeRepository) {
		this.produitRepository = produitRepository;
		this.storeRepository = storeRepository;
	}

	@Transactional(readOnly = true)
	public List<ProduitResponse> findAll() {
		return produitRepository.findAll().stream().map(ProduitResponse::from).toList();
	}

	@Transactional(readOnly = true)
	public ProduitResponse findById(Long id) {
		return ProduitResponse.from(getOrThrow(id));
	}

	@Transactional(readOnly = true)
	public ProduitResponse findByCode(String code) {
		return produitRepository.findByCode(code).map(ProduitResponse::from)
				.orElseThrow(() -> new EntityNotFoundException("Produit introuvable : code=" + code));
	}

	@Transactional(readOnly = true)
	public List<ProduitResponse> findByStore(Long storeId) {
		return produitRepository.findByStoreId(storeId).stream().map(ProduitResponse::from).toList();
	}

	@Transactional
	public ProduitResponse create(ProduitRequest request) {
		Produit produit = new Produit(request.name(), request.code());
		produit.setStore(resolveStore(request.store()));
		return ProduitResponse.from(produitRepository.save(produit));
	}

	@Transactional
	public ProduitResponse update(Long id, ProduitRequest request) {
		Produit produit = getOrThrow(id);
		produit.setName(request.name());
		produit.setCode(request.code());
		produit.setStore(resolveStore(request.store()));
		// saveAndFlush : le cascade PERSIST du nouveau store s'exécute au flush, avant de construire la réponse
		return ProduitResponse.from(produitRepository.saveAndFlush(produit));
	}

	@Transactional
	public void delete(Long id) {
		produitRepository.delete(getOrThrow(id));
	}

	private Produit getOrThrow(Long id) {
		return produitRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Produit introuvable : id=" + id));
	}

	// Un store existant avec le même code est réutilisé (sa description n'est pas modifiée), sinon un nouveau
	// Store est retourné et sauvegardé avec le produit grâce au cascade PERSIST de Produit.store.
	private Store resolveStore(StoreRequest request) {
		if (request == null || !StringUtils.hasText(request.code())) {
			throw new IllegalArgumentException("store.code est obligatoire");
		}
		return storeRepository.findByCode(request.code())
				.orElseGet(() -> new Store(request.code(), request.description()));
	}

}
