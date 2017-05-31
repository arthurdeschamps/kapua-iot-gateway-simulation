# kapua-iot-gateway-simulation
Supply chain management simulator for kapua. The goal is to build a parametrisable simulator for quick tests of the kapua platform.

## Getting started
Install redis
 - On mac os (with brew): 
    ```# brew install redis```
 - Other platforms: see this [page](https://redis.io/topics/quickstart)
 
Start redis:

    # redis-server /usr/local/etc/redis.conf
Run project

### More
In order to be able to see the model's parameters (products, 
employees, vehicles, etc), you can install redis-commander:

    // install via npm
    # npm install -g redis-commander
    // run on localhost
    # redis-commander
## Technologies
### Database
Redis
