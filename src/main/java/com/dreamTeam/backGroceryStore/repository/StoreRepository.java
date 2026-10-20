package com.dreamTeam.backGroceryStore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dreamTeam.backGroceryStore.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {

	Optional<Store> findByCode(String code);

}
