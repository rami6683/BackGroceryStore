package com.dreamTeam.backGroceryStore.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Produit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(unique = true)
	private String code;

	@OneToMany(mappedBy = "produit", cascade = CascadeType.REMOVE)
	private List<ProduitStock> stocks = new ArrayList<>();

	public Produit() {
	}

	public Produit(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<ProduitStock> getStocks() {
		return stocks;
	}

	public void setStocks(List<ProduitStock> stocks) {
		this.stocks = stocks;
	}

}
