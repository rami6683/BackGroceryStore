package com.dreamTeam.backGroceryStore.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dreamTeam.backGroceryStore.dto.ProduitStockRequest;
import com.dreamTeam.backGroceryStore.dto.ProduitStockResponse;
import com.dreamTeam.backGroceryStore.service.ProduitStockService;

@RestController
@RequestMapping("/produitStocks")
public class ProduitStockController {

	private final ProduitStockService produitStockService;

	public ProduitStockController(ProduitStockService produitStockService) {
		this.produitStockService = produitStockService;
	}

	@GetMapping
	public List<ProduitStockResponse> findAll() {
		return produitStockService.findAll();
	}

	@GetMapping("/{id}")
	public ProduitStockResponse findById(@PathVariable Long id) {
		return produitStockService.findById(id);
	}

	@GetMapping("/by-produit")
	public List<ProduitStockResponse> findByProduit(@RequestParam Long produitId) {
		return produitStockService.findByProduit(produitId);
	}

	@PostMapping
	public ResponseEntity<ProduitStockResponse> create(@RequestBody ProduitStockRequest request) {
		ProduitStockResponse created = produitStockService.create(request);
		return ResponseEntity
				.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.id()).toUri())
				.body(created);
	}

	@PutMapping("/{id}")
	public ProduitStockResponse update(@PathVariable Long id, @RequestBody ProduitStockRequest request) {
		return produitStockService.update(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		produitStockService.delete(id);
	}

}
