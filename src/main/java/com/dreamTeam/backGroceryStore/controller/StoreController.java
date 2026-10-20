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

import com.dreamTeam.backGroceryStore.dto.StoreRequest;
import com.dreamTeam.backGroceryStore.dto.StoreResponse;
import com.dreamTeam.backGroceryStore.service.StoreService;

@RestController
@RequestMapping("/stores")
public class StoreController {

	private final StoreService storeService;

	public StoreController(StoreService storeService) {
		this.storeService = storeService;
	}

	@GetMapping
	public List<StoreResponse> findAll() {
		return storeService.findAll();
	}

	@GetMapping("/{id}")
	public StoreResponse findById(@PathVariable Long id) {
		return storeService.findById(id);
	}

	@GetMapping("/by-code")
	public StoreResponse findByCode(@RequestParam String code) {
		return storeService.findByCode(code);
	}

	@PostMapping
	public ResponseEntity<StoreResponse> create(@RequestBody StoreRequest request) {
		StoreResponse created = storeService.create(request);
		return ResponseEntity
				.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.id()).toUri())
				.body(created);
	}

	@PutMapping("/{id}")
	public StoreResponse update(@PathVariable Long id, @RequestBody StoreRequest request) {
		return storeService.update(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		storeService.delete(id);
	}

}
