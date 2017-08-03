

General information
=======
## What is this simulation about ? ##
This supply chain control simulation (SCCS) is a showcase for [Kapua](https://eclipse.org/kapua/). What is Kapua ? As stated on welcome page of the platform's website :

> Modular IoT cloud platform to manage and integrate devices and their data.

With this simulation, you are able in a few manipulations to run the simulation and send auto-generated data to the platform via the [Kapua gateway client](https://github.com/ctron/kapua-gateway-client).

The telemetry data is also available in a user friendly [web application](../webapp-angular/), a kind of dashboard. This dashboard also enables the user to [parametrise](../simulator/src/main/java/simulation/main/Parametrizer.java) the simulation, that is to change parameters that directly affect the simulation's behaviours.

## What is the simulation's flow ? ##
SCCS simulates a [company](../simulator/src/main/java/company/company/Company.java) evolving in a fluctuating [economy](../simulator/src/main/java/economy/Economy.java). The company has customers, transports, product types and products, orders, etc. 

The evolution that follows the company is directly affected by the economy. For instance, if the global demand grows bigger, then the number of customers will certainly grow too, as well as the number of orders. Of course, the [randomness factor](../simulator/src/main/java/simulation/util/ProbabilityUtils.java) might play a decisive role. As in real life, even if something goes wrong economically-wise, a particular company might not follow the trend and on the contrary receive many orders, turns up its production, and so on.

The economy is affected in great part by randomness, but also by itself. If at some point the chance goes in favor of the economy and there is a high growth, this latter might stand for a little while but will eventually slow down, or even back down. Globally, this simulation tries be as close as what can be observed in the history of economy, in terms of trends. 

In order to prevent people from waiting a week before the company gets its first customer, the company is [generated](../simulator/src/main/java/simulation/generators/CompanyGenerator.java) with existing "data" (in other words, the company will already have customers, product types, etc) and the basic *time flow* is set to 3600, but can be tweaked within the web app.

*time flow* = Speed of virtual time against real time. 3600 = 3600 times faster than real life.

## How is the simulation structured ? ##
The SCCS is composed of three modules.

 1. [**Simulator**](../simulator/) : The simulator generates the company, the economy and make them evolve in accordance to a few parameters as well as randomness. Besides that, it takes care of publishing the generated data to Kapua at a fixed interval of time.  Finally, it also communicates directly with the UI by providing application-related data such as the company's name, transports information and many other things. The UI can also tell the simulator to modify its parameters, for example if the user increases the *time flow*.
 
 2. [**Data Transmitter**](../data-transmitter/): The data transmitter listens to the publications made by the simulator and sends them all to the web application.

 3. [**Web application**](../webapp-angular/):  The web-based application is a simple dashboard, displaying deliveries in real-time on an interactive map and showing some quantities evolution over time (e.g. customers). The *parametrizer* section enables the user to modify the simulation's parameters.
