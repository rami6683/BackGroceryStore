package com.dreamTeam.backGroceryStore.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dreamTeam.backGroceryStore.entity.Produit;
import com.dreamTeam.backGroceryStore.service.ProduitService;

@RestController
public class ProduitController {

	private final ProduitService produitService;

	public ProduitController(ProduitService produitService) {
		this.produitService = produitService;
	}

	// Chemin /api/produits : mapper POST /produits ici ferait répondre 405 au GET /produits de Spring Data REST.
	@PostMapping("/api/produits")
	@ResponseStatus(HttpStatus.CREATED)
	public Produit create(@RequestBody Produit produit) {
		return produitService.create(produit);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public void handleConflict() {
	}

}
