FROM maven:3.9-eclipse-temurin-21 AS quarkus

RUN apt-get update && \
    apt-get install -y ca-certificates curl gnupg && \
    mkdir -p /etc/apt/keyrings && \
    curl -fsSL https://deb.nodesource.com/gpgkey/nodesource-repo.gpg.key | gpg --dearmor -o /etc/apt/keyrings/nodesource.gpg && \
    echo "deb [signed-by=/etc/apt/keyrings/nodesource.gpg] https://deb.nodesource.com/node_18.x nodistro main" | tee /etc/apt/sources.list.d/nodesource.list && \
    apt-get update && \
    apt-get install -y nodejs

WORKDIR /app

COPY .mvn/ .mvn/
COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY /src ./src

RUN mvn clean package -Dquarkus.package.type=uber-jar -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /deploy

COPY --from=quarkus /app/target/*-runner.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]