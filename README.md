# Eldorado
Java lightweight web microframework inspired by Laravel and based on Jetty. 
It is intended to design microservices communicating through HTTP/REST.

Features:
- Laravel style web routes definition (/people/{name})
- Basic DI functionality
- Health Check
- Basic middleware
- Closures can be used to handle HTTP response

In the roadmap:
- ENV based config
- Hashicorp Consul connect for service discovery
- DB/ORM (Entities, JDBC based query builder, SQL execution)
- JWT auth

Under consideration:
- Protobuf communication
- Apache Kafka connect module
- Unified message format with JSON schema validation
- RESTful resource routes
- DB migrations
