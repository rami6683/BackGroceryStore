# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Spring Boot 4.1.1 backend (Java 17, Maven) for a grocery store, base package `com.dreamTeam.backGroceryStore`. Dependencies: `spring-boot-starter-webmvc`, `spring-boot-starter-data-jpa`, `spring-boot-starter-data-rest`, H2 (runtime scope, in-memory). No datasource is configured in `application.properties`, so Spring Boot uses its embedded H2 defaults.

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

- `entity/` — JPA entities (`@Entity`, `Long` id with `GenerationType.IDENTITY`, plain getters/setters, no Lombok). Currently `Store` (`id`, `code`, `description`), the top-level grouping: every `Produit` and every `Stock` belongs to exactly one `Store` (mandatory `store` URI when creating them via Spring Data REST; filter with `/produits/search/by-store?storeId=` and `/stocks/search/by-store?storeId=`). Then `Produit` (`id`, `name`, unique `code`, `store`), `Stock` (`id`, `code`, `description`, `store`) and `ProduitStock`, the association entity linking a product to a stock with a `quantite`. A product has a list of `ProduitStock`; the `(produit_id, stock_id)` pair is unique, so a product is linked at most once to a given stock. Links are created through Spring Data REST by POSTing to `/produitStocks` with `produit` and `stock` as URIs.
- `repository/` — Spring Data interfaces extending `JpaRepository<Entity, Long>`, used for CRUD (e.g. `ProduitRepository`).

Entity and code naming mixes French (`Produit`) and English field names (`name`, `code`).

### REST layer (controller part)

There are no hand-written controllers or services for CRUD. Endpoints come from `spring-boot-starter-data-rest`, which exposes each repository automatically (e.g. `/produits`, `/produits/{id}`). To add behaviour beyond the CRUD it provides, go down this list and stop at the first option that works:

1. **Named query methods** in the JPA repository (e.g. `Optional<Produit> findByCode(String code)`), exposed by Spring Data REST under `/{resource}/search/...`. Use `@RestResource(path = "...")` to control the URL.
2. **JPQL** (`@Query`) methods in the repository when the query cannot be expressed as a named method.
3. **Custom `@RestController`** only when the endpoint is genuinely custom, i.e. a non-CRUD `POST` (an action or business operation rather than create/read/update/delete of an entity).

## Git workflow

Changes go through pull requests from a feature branch off `master` (see merged PRs #1 and #2). The `gh` CLI is not installed on this machine, so PRs are opened via the GitHub compare URL after pushing the branch.
