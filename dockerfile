FROM eclipse-temurin:21.0.6_7-jdk-ubi9-minimal AS builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests -Pprod

FROM eclipse-temurin:21.0.6_7-jre-ubi9-minimal
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]