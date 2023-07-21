FROM openjdk:17.0.1-jdk-slim

RUN apt-get update
RUN apt-get install -y maven

# navega para a pasta app
WORKDIR /app

# Copie todos os arquivos do projeto para a pasta "APP" dentro do container
COPY . /app

# Execute o comando Maven para compilar o projeto
RUN mvn clean install

# Roda o programa
CMD java -jar target/blog-0.0.1-SNAPSHOT.jar