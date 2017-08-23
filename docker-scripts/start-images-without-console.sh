#!/bin/bash
docker stop $(docker ps -a -q)
docker start kapua-sql
docker start kapua-elasticsearch
docker start kapua-broker
cd kapua/console
mvn org.eclipse.jetty:jetty-maven-plugin:9.4.2.v20170220:run-exploded -nsu -Pdev -DuseTestScope=true -Djetty.daemon=false -Dcommons.db.connection.host=localhost
