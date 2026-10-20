package com.dreamTeam.backGroceryStore.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dreamTeam.backGroceryStore.dto.ProduitStockRequest;
import com.dreamTeam.backGroceryStore.dto.ProduitStockResponse;
import com.dreamTeam.backGroceryStore.entity.Produit;
import com.dreamTeam.backGroceryStore.entity.ProduitStock;
import com.dreamTeam.backGroceryStore.entity.Stock;
import com.dreamTeam.backGroceryStore.repository.ProduitRepository;
import com.dreamTeam.backGroceryStore.repository.ProduitStockRepository;
import com.dreamTeam.backGroceryStore.repository.StockRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProduitStockService {

	private final ProduitStockRepository produitStockRepository;

	private final ProduitRepository produitRepository;

	private final StockRepository stockRepository;

	public ProduitStockService(ProduitStockRepository produitStockRepository, ProduitRepository produitRepository,
			StockRepository stockRepository) {
		this.produitStockRepository = produitStockRepository;
		this.produitRepository = produitRepository;
		this.stockRepository = stockRepository;
	}

	@Transactional(readOnly = true)
	public List<ProduitStockResponse> findAll() {
		return produitStockRepository.findAll().stream().map(ProduitStockResponse::from).toList();
	}

	@Transactional(readOnly = true)
	public ProduitStockResponse findById(Long id) {
		return ProduitStockResponse.from(getOrThrow(id));
	}

	@Transactional(readOnly = true)
	public List<ProduitStockResponse> findByProduit(Long produitId) {
		return produitStockRepository.findByProduitId(produitId).stream().map(ProduitStockResponse::from).toList();
	}

	@Transactional
	public ProduitStockResponse create(ProduitStockRequest request) {
		ProduitStock produitStock = new ProduitStock();
		fill(produitStock, request);
		return ProduitStockResponse.from(produitStockRepository.save(produitStock));
	}

	@Transactional
	public ProduitStockResponse update(Long id, ProduitStockRequest request) {
		ProduitStock produitStock = getOrThrow(id);
		fill(produitStock, request);
		return ProduitStockResponse.from(produitStockRepository.save(produitStock));
	}

	@Transactional
	public void delete(Long id) {
		produitStockRepository.delete(getOrThrow(id));
	}

	private ProduitStock getOrThrow(Long id) {
		return produitStockRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("ProduitStock introuvable : id=" + id));
	}

	private void fill(ProduitStock produitStock, ProduitStockRequest request) {
		if (request.produitId() == null || request.stockId() == null || request.quantite() == null) {
			throw new IllegalArgumentException("produitId, stockId et quantite sont obligatoires");
		}
		Produit produit = produitRepository.findById(request.produitId())
				.orElseThrow(() -> new IllegalArgumentException("Produit introuvable : id=" + request.produitId()));
		Stock stock = stockRepository.findById(request.stockId())
				.orElseThrow(() -> new IllegalArgumentException("Stock introuvable : id=" + request.stockId()));
		produitStock.setProduit(produit);
		produitStock.setStock(stock);
		produitStock.setQuantite(request.quantite());
	}

}
