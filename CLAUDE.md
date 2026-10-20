# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Spring Boot 4.1.1 backend (Java 17, Maven) for a grocery store, base package `com.dreamTeam.backGroceryStore`. Dependencies: `spring-boot-starter-webmvc`, `spring-boot-starter-data-jpa`, `spring-boot-starter-data-rest` (still in the pom but being phased out, see "REST layer"), H2 (runtime scope, in-memory). No datasource is configured in `application.properties`, so Spring Boot uses its embedded H2 defaults.

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

Docker: `docker compose up --build -d` starts MySQL (`db`, data in the `mysql_data` volume) and the app (`app`, built from the multi-stage `Dockerfile`) on port 8080; `docker compose down` stops them and keeps the data (add `-v` to wipe it). The compose file overrides the `localhost` datasource of `application.properties` through `SPRING_DATASOURCE_*` environment variables. The image build uses `-DskipTests` because `contextLoads` needs a reachable MySQL.

Kubernetes: `k8s/store.yaml` holds everything (Secret, PVC, MySQL Deployment + Service `db`, app Deployment + NodePort Service) and mirrors the compose file. On a local cluster such as Docker Desktop: `docker build -t mon-app-spring:0.0.1 .`, then `kubectl apply -f k8s/store.yaml`; the API is on `http://localhost:30080`. `kubectl delete -f k8s/store.yaml` removes everything, including the database volume. The manifest uses `imagePullPolicy: IfNotPresent`, so the image must exist in the cluster's image store (with minikube/kind, load it first).

## Architecture

Layered package layout under the base package, one sub-package per layer:

- `entity/` — JPA entities (`@Entity`, `Long` id with `GenerationType.IDENTITY`, plain getters/setters, no Lombok). Currently `Store` (`id`, `code`, `description`), the top-level grouping: every `Produit` and every `Stock` belongs to exactly one `Store` (a store is mandatory when creating them). Then `Produit` (`id`, `name`, unique `code`, `store`), `Stock` (`id`, `code`, `description`, `store`) and `ProduitStock`, the association entity linking a product to a stock with a `quantite`. A product has a list of `ProduitStock`; the `(produit_id, stock_id)` pair is unique, so a product is linked at most once to a given stock.
- `repository/` — Spring Data interfaces extending `JpaRepository<Entity, Long>` (e.g. `ProduitRepository`); only called by services.
- `service/` — `@Service` classes holding the business logic and transactions; only call repositories.
- `controller/` — `@RestController` classes exposing the HTTP endpoints; only call services.
- `dto/` — request/response records exchanged by the controllers.

Entity and code naming mixes French (`Produit`) and English field names (`name`, `code`).

### REST layer

Endpoints, CRUD included, are written by hand, one stack per entity: `XxxController` -> `XxxService` -> `XxxRepository`. We no longer use `spring-boot-starter-data-rest` to generate them.

- `XxxController`: `@RestController` with `@RequestMapping("/xxx")` exposing `GET` (list), `GET /{id}`, `POST`, `PUT /{id}` and `DELETE /{id}`. It only calls the service and exchanges DTO records from `dto/`, not entities.
- `XxxService`: `@Service` with the business rules; `@Transactional` on methods that write. It only calls repositories.
- `XxxRepository`: `JpaRepository<Xxx, Long>`.

For a query that `JpaRepository` does not provide, add it to the repository and stop at the first option that works: 1. a **named query method** (e.g. `Optional<Produit> findByCode(String code)`), 2. a **JPQL** `@Query` method when the query cannot be expressed by name. The service wraps it and the controller exposes it (e.g. `GET /produits/by-code?code=`). A non-CRUD operation (an action rather than create/read/update/delete) is a dedicated controller method that calls a service method.

#### Migration in progress

The code has not been migrated yet. `spring-boot-starter-data-rest` is still in `pom.xml` and still generates the CRUD endpoints of `/produits`, `/stores`, `/stocks` and `/produitStocks` (HAL responses, associations given as URIs, `/search/...` endpoints such as `/produits/search/by-code?code=` and `/produits/search/by-store?storeId=`). The only hand-written stack is `ProduitController` (`@RepositoryRestController`) + `ProduitService` for `POST /produits`. To migrate an entity: write its controller, service and DTOs, hide its generated endpoints with `@RepositoryRestResource(exported = false)` on the repository, and remove the dependency from `pom.xml` once every entity is done. While the dependency is still present, a plain `@RestController` mapped on a path Spring Data REST also serves (e.g. exactly `/produits`) makes the generated `GET /produits` answer 405, because Spring MVC answers 405 as soon as the path matches with another method; use `@RepositoryRestController` or a different path until then.

The existing `POST /produits` replaces the Spring Data REST one so the `store` can be described in the JSON instead of given as a URI: `{"name":"Tomate","code":"P1","store":{"code":"ST-A","description":"Magasin A"}}`. The body is bound to the `ProduitRequest` DTO (not the entity), because Spring Data REST's JSON mapping rejects an embedded association object. `Store.code` is unique and `ProduitService.create` looks the store up by that code: if one exists it is reused (the description in the request is ignored), otherwise a new `Store` is saved with the product through `Produit.store`'s `cascade = PERSIST`. Reuse is by `code` only, so a store `id` cannot be sent. Returns 201 with a plain `application/json` `ProduitResponse` (`id`, `name`, `code` and a nested `store` object with `id`, `code`, `description`; no HAL `_links`) and a `Location: /produits/{id}` header, 400 (missing `store.code`) or 409 (duplicate product `code`, nothing is created). The response is deliberately not HAL, unlike the other `/produits` endpoints. The old `POST /produits` with `store` as a URI no longer works, while the generated `PUT`/`PATCH` still take the URI.

## Git workflow

Changes go through pull requests from a feature branch off `master` (see merged PRs #1 and #2). The `gh` CLI is not installed on this machine, so PRs are opened via the GitHub compare URL after pushing the branch.
