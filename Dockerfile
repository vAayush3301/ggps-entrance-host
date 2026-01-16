FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

COPY . .

RUN chmod +x gradlew

RUN ./gradlew clean build -x test

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
