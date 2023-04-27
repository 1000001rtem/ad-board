# Ad Board

Demo project for ad board application

## Using technologies
- Java
- Spring Boot
- WebFlux
- R2DBC
- Postgres
- KeyCloak
- TypeScript
- React


## Dependencies
- Required docker
- Required npm
- Required starting keycloak on port 8080

## Usage

### KeyCloak
- run: `./keycloak.sh`

### Backend
- build: `./gradlew clean assemble`
- tests: `./gradlew clean test`
- run: `./gradlew bootRun`

### Frontend
- install: `npm install`
- run: `npm start`
- build: `npm build`

## Links
[repository](https://github.com/1000001rtem/ad-board)

[keycloak realm](realm-export.json)

## Contacts
Author: Eremin Artem

Site: [1000001rtem.com](http://1000001rtem.com)

Email: 1000001rtem@gmail.com
