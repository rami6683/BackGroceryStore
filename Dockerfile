# ---------- Étape 1 : build du .jar avec Maven + JDK 17 ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# On copie le pom et les sources, puis on package l'application.
# -DskipTests : les tests ont besoin d'une base MySQL, absente pendant le build.
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

# ---------- Étape 2 : image finale avec uniquement un JRE 17 et le .jar ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

# On ne récupère que le .jar produit à l'étape 1 (ni Maven, ni JDK, ni sources).
COPY --from=build /app/target/*.jar app.jar

# Port HTTP de Spring Boot.
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
