#!/bin/bash
docker stop $(docker ps -a -q)
docker start kapua-sql
docker start kapua-elasticsearch
docker start kapua-broker
docker start kapua-console
docker start kapua-api
