# 1) BUILD STAGE
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -B -f pom.xml dependency:go-offline
COPY src ./src

RUN mvn clean package -DskipTests

# 2) RUN STAGE (lightweight)
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

#copy jar (assumea single jar product)
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080 
#ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java","-jar","/app.jar"]