FROM maven:3.8.5-openjdk-17

WORKDIR /beana-app
COPY . .
RUN mvn clean install
EXPOSE 8686

CMD mvn spring-boot:run