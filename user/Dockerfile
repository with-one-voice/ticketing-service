# Build stage
FROM openjdk:17-jdk-slim AS builder

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew :user:clean :user:build -x test

# Run stage
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/user/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
