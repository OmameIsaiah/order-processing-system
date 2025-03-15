
@echo off

REM Shutdown or stop any previous kafka, zookeeper and mysql servers that is running
call docker-compose -f docker/dev-services.yml down

REM Create the docker network for the application
call docker network create --driver bridge dev_order_ps_network

REM Build the docker containers for kafka, zookeeper and mysql servers
call docker-compose -f docker/dev-services.yml up -d --build

REM Shutdown or stop any previous docker container/services that are running
call docker-compose -f account-service/docker-compose-dev.yml down
call docker-compose -f order-service/docker-compose-dev.yml down
call docker-compose -f inventory-service/docker-compose-dev.yml down
call docker-compose -f notification-service/docker-compose-dev.yml down
call docker-compose -f config-service/docker-compose-dev.yml down

REM Build the docker images using the Dockerfile, then create a container with the image and start it
call docker-compose -f config-service/docker-compose-dev.yml up -d --build
call docker-compose -f notification-service/docker-compose-dev.yml up -d --build
call docker-compose -f inventory-service/docker-compose-dev.yml up -d --build
call docker-compose -f order-service/docker-compose-dev.yml up -d --build
call docker-compose -f account-service/docker-compose-dev.yml up -d --build

REM press ctl+c to stop  docker container in an interactive mode
