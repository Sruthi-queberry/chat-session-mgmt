# RAG Chat Storage Microservice

Backend microservice for storing and managing chat sessions and messages for a **RAG-based chatbot**.

## Table of Contents

* [Features](#features)
* [Architecture & Tech Stack](#architecture--tech-stack)
* [Prerequisites](#prerequisites)
* [Setup & Run](#setup--run)
* [Environment Variables](#environment-variables)
* [APIs](#apis)
* [Swagger / OpenAPI](#swagger--openapi)
* [Testing](#testing)
* [Docker](#docker)
* [Notes](#notes)

## Features

* Start and maintain multiple chat sessions per user
* Save chat messages with sender and content
* Rename sessions, mark/unmark as favorite, delete sessions
* Cursor-based pagination for message retrieval
* API key authentication for security
* Rate limiting using Redis
* Centralized logging and global error handling
* Health checks via Spring Actuator
* Dockerized for easy deployment

## Architecture & Tech Stack

* **Language:** Java 21
* **Framework:** Spring Boot 3 / Spring Web / Spring Data JPA
* **Database:** PostgreSQL
* **Cache / Rate Limiting:** Redis
* **Authentication:** API key-based
* **Documentation:** Swagger / OpenAPI
* **Containerization:** Docker & Docker Compose
* **Logging:** SLF4J (Lombok)


## Prerequisites

* Docker & Docker Compose
* JDK 21
* Maven

## Setup & Run

### Clone the repository

```bash
git clone <your-repo-url>
cd chat-session-mgmt
```

### Build & Run with Docker Compose

Dockerfile and compose.yaml are available in the root folder

```bash
docker-compose up --build
```

This will start:

* PostgreSQL on port `5432`
* pgAdmin on port `5431`
* Redis on port `6379`
* Chat microservice on port `8081`

## Environment Variables

Create a `.env` file in the project root
A `.env.example` file is available in the project root

Docker Compose will automatically load `.env`.

## Swagger / OpenAPI

* Swagger is available at:

http://localhost:8081/swagger-ui/index.html

* OpenAPI spec:

http://localhost:8081/v3/api-docs

## Testing

Basic unit tests exist for services. To run tests:

```bash
mvn test
```

## Notes
* Centralized logging with SLF4J and Lombok
* Cursor-based pagination ensures efficient retrieval for large chat histories
