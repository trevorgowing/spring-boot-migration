#!/usr/bin/env bash

set -e

sudo docker-compose up -d mongodb
sudo docker-compose up --build spring-boot-migration
