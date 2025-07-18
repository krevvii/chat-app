# --- Этап 1: Сборка проекта с помощью Maven ---
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install -DskipTests

# --- Этап 2: Создание финального легковесного образа ---
FROM openjdk:21-slim
WORKDIR /app
# Копируем только скомпилированный jar-файл из этапа сборки
COPY --from=build /app/target/*.jar app.jar
# Указываем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]