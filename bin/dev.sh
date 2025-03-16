#!/bin/bash
echo "Loading environment variables from .env"

export $(grep -v '^#' .env | xargs)
./mvnw spring-boot:run