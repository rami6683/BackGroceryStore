# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Spring Boot 4.1.1 backend (Java 17, Maven) for a grocery store, base package `com.dreamTeam.backGroceryStore`. Dependencies: `spring-boot-starter-webmvc`, `spring-boot-starter-data-jpa`, H2 (runtime scope, in-memory). No datasource is configured in `application.properties`, so Spring Boot uses its embedded H2 defaults.

## Commands

Use the Maven wrapper (`mvnw.cmd` on Windows PowerShell, `./mvnw` in Git Bash):

```bash
./mvnw compile                                   # build
./mvnw spring-boot:run                           # run the app
./mvnw test                                      # all tests
./mvnw test -Dtest=BackGroceryStoreApplicationTests            # one test class
./mvnw test -Dtest=BackGroceryStoreApplicationTests#contextLoads  # one test method
```

No lint or formatter is configured.

## Architecture

Layered package layout under the base package, one sub-package per layer:

- `entity/` — JPA entities (`@Entity`, `Long` id with `GenerationType.IDENTITY`, plain getters/setters, no Lombok). Currently `Produit` (`id`, `name`, `code`).
- `repository/` — Spring Data interfaces extending `JpaRepository<Entity, Long>`, used for CRUD (e.g. `ProduitRepository`).

There are no services or controllers yet; `spring-boot-starter-webmvc` is present for the upcoming REST layer. Entity and code naming mixes French (`Produit`) and English field names (`name`, `code`).

## Git workflow

Changes go through pull requests from a feature branch off `master` (see merged PRs #1 and #2). The `gh` CLI is not installed on this machine, so PRs are opened via the GitHub compare URL after pushing the branch.
