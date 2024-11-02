# Verve - Tech Challenge 
### Quick start
You can start the application using `docker compose`, which will launch the PostgreSQL database, artemis and the app. 
1. ```cd docker/docker-compose/ && docker compose up```

### The task was both intriguing and challenging. For example, it involved developing an algorithm that identifies unique requests based on their IDs for each minute.

### 1. Requests are stored with a unique identifier to associate them with the corresponding minute.
### 2. Duplicate requests are avoided by employing transactions.
### 3. Spring Boot initiates the ["TrackerJob"](https://github.com/zafarius/request-logger-app/blob/main/tracker/messaging/src/main/java/app/messaging/tracker/TrackerJob.java) every minute, which sends a JMS message containing the request count, subsequently consumed by ["CountReceiver"](https://github.com/zafarius/request-logger-app/blob/main/tracker/messaging/src/main/java/app/messaging/tracker/CountReceiver.java).

### Overview architecture
The architecture employs the port-adapter pattern, also known as hexagonal architecture, to create loosely coupled components. For example, the domain tracker includes a core domain that encompasses all the necessary business logic. This core is utilized by various adapters, such as repositories and web services.

#### Used tech stack: Java, Spring-Boot, PostgresSql, Docker, Flyway, Artemis as the message broker, Transactions with Atomikos etc.
