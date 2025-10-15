# Gateway Server

This is the API Gateway service for the FinBank microservices application.

## Description

The Gateway Server provides a single entry point for all client requests and routes them to the appropriate microservices. It uses Spring Cloud Gateway for routing and load balancing.

## Features

- Service discovery integration with Eureka
- Dynamic routing to microservices
- Load balancing
- Centralized configuration with Config Server

## Port

Default port: 8072

## Routes

- `/api/accounts/**` -> ACCOUNTS service
- `/api/loans/**` -> LOANS service
- `/api/cards/**` -> CARDS service

