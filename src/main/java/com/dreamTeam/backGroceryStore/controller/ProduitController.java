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

import com.dreamTeam.backGroceryStore.dto.ProduitRequest;
import com.dreamTeam.backGroceryStore.dto.ProduitResponse;
import com.dreamTeam.backGroceryStore.service.ProduitService;

@RestController
@RequestMapping("/produits")
public class ProduitController {

	private final ProduitService produitService;

	public ProduitController(ProduitService produitService) {
		this.produitService = produitService;
	}

	@GetMapping
	public List<ProduitResponse> findAll() {
		return produitService.findAll();
	}

	@GetMapping("/{id}")
	public ProduitResponse findById(@PathVariable Long id) {
		return produitService.findById(id);
	}

	@GetMapping("/by-code")
	public ProduitResponse findByCode(@RequestParam String code) {
		return produitService.findByCode(code);
	}

	@GetMapping("/by-store")
	public List<ProduitResponse> findByStore(@RequestParam Long storeId) {
		return produitService.findByStore(storeId);
	}

	@PostMapping
	public ResponseEntity<ProduitResponse> create(@RequestBody ProduitRequest request) {
		ProduitResponse created = produitService.create(request);
		return ResponseEntity
				.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.id()).toUri())
				.body(created);
	}

	@PutMapping("/{id}")
	public ProduitResponse update(@PathVariable Long id, @RequestBody ProduitRequest request) {
		return produitService.update(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		produitService.delete(id);
	}

}
