#!/bin/bash
echo "Loading environment variables from .env.local"

export $(grep -v '^#' .env.local | xargs)
./mvnw spring-boot:run