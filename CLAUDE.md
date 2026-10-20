# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Spring Boot 4.1.1 backend (Java 17, Maven) for a grocery store, base package `com.dreamTeam.backGroceryStore`. Dependencies: `spring-boot-starter-webmvc`, `spring-boot-starter-data-jpa`, and at runtime the MySQL driver (`mysql-connector-j`) and H2. `application.properties` configures a MySQL datasource (`jdbc:mysql://localhost:3306/storeDB`, user `root`) with `spring.jpa.hibernate.ddl-auto=update` and `spring.jpa.show-sql=true`, so a local MySQL must be running. H2 is only used if you override the datasource (see Commands).

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

To run without MySQL (throwaway in-memory H2, handy for manual endpoint tests), override the datasource and, if 8080 is taken, the port:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=8081 --spring.datasource.url=jdbc:h2:mem:t --spring.datasource.username=sa --spring.datasource.password="
```

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

Endpoints, CRUD included, are written by hand, one stack per entity: `XxxController` -> `XxxService` -> `XxxRepository`. We do not use `spring-boot-starter-data-rest` (it was removed from the pom; do not add it back).

- `XxxController`: `@RestController` with `@RequestMapping("/xxx")` exposing `GET` (list), `GET /{id}`, `POST` (201 + `Location`), `PUT /{id}` and `DELETE /{id}` (204). It only calls the service and exchanges DTO records from `dto/`, not entities.
- `XxxService`: `@Service` with the business rules; `@Transactional` on every method (`readOnly = true` for reads). It only calls repositories and returns DTOs, built inside the transaction because `Produit.store` and the `ProduitStock` associations are lazy.
- `XxxRepository`: `JpaRepository<Xxx, Long>`.
- `GlobalExceptionHandler` (`@RestControllerAdvice`) turns exceptions into RFC 9457 `ProblemDetail` responses: `EntityNotFoundException` -> 404, `IllegalArgumentException` -> 400, `DataIntegrityViolationException` -> 409 (duplicate unique value, missing store, element still referenced). Services throw these, controllers do not catch them.

Current endpoints (all JSON): `/produits`, `/stores`, `/stocks` and `/produitStocks` with the CRUD above, plus `GET /produits/by-code?code=`, `GET /produits/by-store?storeId=`, `GET /stores/by-code?code=`, `GET /stocks/by-store?storeId=` and `GET /produitStocks/by-produit?produitId=`.

Request bodies: `Produit` and `Stock` carry an embedded `store` object (`{"name":"Tomate","code":"P1","store":{"code":"ST-A","description":"Magasin A"}}`). `Store.code` is unique and the service looks the store up by that code: if one exists it is reused (the description in the request is ignored), otherwise a new `Store` is created with the product/stock through `cascade = PERSIST` on `.store`. Reuse is by `code` only, a store `id` cannot be sent; a missing `store.code` is a 400. In `update`, `saveAndFlush` is used so the cascade runs before the response is built (otherwise a newly created store would come back with a null `id`). A `ProduitStock` request references the two sides by id: `{"produitId":1,"stockId":2,"quantite":10}`; an unknown id is a 400, and the `(produit, stock)` pair is unique (409).

For a query that `JpaRepository` does not provide, add it to the repository and stop at the first option that works: 1. a **named query method** (e.g. `Optional<Produit> findByCode(String code)`), 2. a **JPQL** `@Query` method when the query cannot be expressed by name. The service wraps it and the controller exposes it (e.g. `GET /produits/by-code?code=`). A non-CRUD operation (an action rather than create/read/update/delete) is a dedicated controller method that calls a service method.

## Git workflow

Changes go through pull requests from a feature branch off `master` (see merged PRs #1 and #2). The `gh` CLI is not installed on this machine, so PRs are opened via the GitHub compare URL after pushing the branch.
