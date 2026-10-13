package com.dreamTeam.backGroceryStore.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dreamTeam.backGroceryStore.dto.ProduitRequest;
import com.dreamTeam.backGroceryStore.dto.ProduitResponse;
import com.dreamTeam.backGroceryStore.entity.Produit;
import com.dreamTeam.backGroceryStore.service.ProduitService;

// @RepositoryRestController (et non @RestController) : ce POST remplace celui de Spring Data REST sur /produits
// sans casser le GET /produits ni les autres endpoints générés.
@RepositoryRestController
public class ProduitController {

	private final ProduitService produitService;

	public ProduitController(ProduitService produitService) {
		this.produitService = produitService;
	}

	@PostMapping("/produits")
	public ResponseEntity<ProduitResponse> create(@RequestBody ProduitRequest request) {
		if (request.store() == null || !StringUtils.hasText(request.store().code())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "store.code est obligatoire");
		}
		Produit produit = produitService.create(request);
		// Location : /produits/{id}, construit à partir de l'URL de la requête courante (POST /produits)
		return ResponseEntity
				.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(produit.getId()).toUri())
				.contentType(MediaType.APPLICATION_JSON)
				.body(ProduitResponse.from(produit));
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public void handleConflict() {
	}

}
