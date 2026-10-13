package com.dreamTeam.backGroceryStore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import com.dreamTeam.backGroceryStore.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {

	@RestResource(path = "by-code", rel = "by-code")
	Optional<Store> findByCode(String code);

}
