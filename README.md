# Spring-Zull-Keycloak-SSO

Spring Zull project with configuration to connect with Keycloak and returns a JWT.

### Prerequisites
```
Java
Maven
Keycloak
```
### Before installing

Add your keycloak config at /api-gateway/src/main/resources/keycloak.json \
Add your eureka config at /api-gateway/src/main/resources/application.yml \
Add your DB config at /api-gateway/src/main/resources/application.properties

### Installing

```
mvn clean install
java -jar target/api-gateway-0.0.1.jar
```

### Usage

There is a /login REST endpoint.

```
Input:
username and pasword

Output:
JWT with info from DB
```


