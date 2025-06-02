# Multi-stage build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy the entire project (parent pom and all modules)
COPY . .

# Build only this module and its dependencies
RUN mvn clean package -DskipTests -pl db-migration -am

# Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the jar file
COPY --from=build /app/db-migration/target/db-migration-1.0.0.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"] 