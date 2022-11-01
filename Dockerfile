FROM openjdk:17-jdk-slim
COPY "./build/libs/api-users-1.0.0.jar" "api-users.jar"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "api-users.jar"]