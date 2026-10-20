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

import com.dreamTeam.backGroceryStore.dto.StockRequest;
import com.dreamTeam.backGroceryStore.dto.StockResponse;
import com.dreamTeam.backGroceryStore.service.StockService;

@RestController
@RequestMapping("/stocks")
public class StockController {

	private final StockService stockService;

	public StockController(StockService stockService) {
		this.stockService = stockService;
	}

	@GetMapping
	public List<StockResponse> findAll() {
		return stockService.findAll();
	}

	@GetMapping("/{id}")
	public StockResponse findById(@PathVariable Long id) {
		return stockService.findById(id);
	}

	@GetMapping("/by-store")
	public List<StockResponse> findByStore(@RequestParam Long storeId) {
		return stockService.findByStore(storeId);
	}

	@PostMapping
	public ResponseEntity<StockResponse> create(@RequestBody StockRequest request) {
		StockResponse created = stockService.create(request);
		return ResponseEntity
				.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.id()).toUri())
				.body(created);
	}

	@PutMapping("/{id}")
	public StockResponse update(@PathVariable Long id, @RequestBody StockRequest request) {
		return stockService.update(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		stockService.delete(id);
	}

}
