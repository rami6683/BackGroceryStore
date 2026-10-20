package com.dreamTeam.backGroceryStore.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(EntityNotFoundException.class)
	public ProblemDetail handleNotFound(EntityNotFoundException e) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ProblemDetail handleBadRequest(IllegalArgumentException e) {
		return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
	}

	// Doublon sur une colonne unique, store manquant, ou élément encore référencé par un autre (suppression).
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ProblemDetail handleConflict() {
		return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
				"Contrainte d'intégrité violée : doublon, valeur manquante ou élément encore référencé");
	}

}
