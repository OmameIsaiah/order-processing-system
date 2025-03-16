#!/usr/bin/env bash

echo "Shutdown or stop any previous kafka, zookeeper and mysql servers that is running"
docker-compose -f docker/prod-services.yml down

echo "Create the docker network for the application"
docker network create --driver bridge --subnet=173.21.0.0/16 --ip-range=173.21.1.0/24 prod_order_ps_network

echo "Build the docker containers for kafka, zookeeper and mysql servers"
docker-compose -f docker/prod-services.yml up -d --build

echo "Shutdown or stop any previous docker container/services that are running"
docker-compose -f account-service/docker-compose-prod.yml down
docker-compose -f order-service/docker-compose-prod.yml down
docker-compose -f inventory-service/docker-compose-prod.yml down
docker-compose -f notification-service/docker-compose-prod.yml down
docker-compose -f config-service/docker-compose-prod.yml down

echo "Build the docker images using the Dockerfile, then create a container with the image and start it"
docker-compose -f config-service/docker-compose-prod.yml up -d --build
docker-compose -f notification-service/docker-compose-prod.yml up -d --build
docker-compose -f inventory-service/docker-compose-prod.yml up -d --build
docker-compose -f order-service/docker-compose-prod.yml up -d --build
docker-compose -f account-service/docker-compose-prod.yml up -d --build
