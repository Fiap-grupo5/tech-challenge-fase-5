#!/bin/bash

echo "Building and running healthcare microservices..."

# Build all services
echo "Building services..."
mvn clean package -DskipTests

# Run docker-compose
echo "Starting containers..."
docker-compose up --build -d

echo "Done! Services are starting up."
echo "You can check the status using: docker-compose ps"
echo "View logs using: docker-compose logs -f [service-name]"
