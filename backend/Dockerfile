# Используйте официальный образ Java
FROM openjdk:17-jdk-alpine

# Укажите рабочую директорию в контейнере
WORKDIR /app

# Скопируйте файл .jar из целевой папки в рабочую директорию
COPY target/backend-0.0.1-SNAPSHOT.jar backend.jar

# Запустите приложение
CMD ["java", "-jar", "backend.jar"]