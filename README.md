# kapua-iot-gateway-simulation
Supply chain management simulator for kapua. The goal is to build a parametrisable simulator for quick tests of the kapua platform.

## Status
The global simulation is runnable. It uses 3 main component: company, economy and delivery transport simulations.

Company and economy simulations are runnable, eventhough there is still work to perfect them and make them more realistic and adaptive to the different parameters of the global simulation.

The delivery transport simulation runs and probably won't change much over time.

A structure to send data to the Kapua platform exists, although it's still very incomplete.


## Start the simulation
Different sorts of simulations can be run and are all located in main.simulator.main.

## Technologies
### Language
Java

### Test
JUnit with Java

### Build automation tool
Apache Maven
