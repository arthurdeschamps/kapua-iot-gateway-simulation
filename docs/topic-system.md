Topic system
=======

# What is a topic ? #
In this simulation, the term "topic" is used in different contexts. However, it is always a series of words, attached together to form some kind of path, such as `my/fabulous/topic`.

Firstly, it is commonly used in the MQTT world for publications and subscriptions. It defines what kind of information is being sent. If we want to send the temperature in "bedroom A", then we could use the topic `house/temperature/bedroom/A` along with a sample (temp=30), for instance.

Secondly, topic is also used within the WebSocket system. It allows to describe what application data is sent to the frontend (e.g. `company/name` -> `("name", COMPANY_NAME)`). This way, angular has a way to parse the data and recreate the object that was initially converted to a string and sent through the socket. It also allows the frontend to send requests to the module **simulator**, such as _sets_ or _gets_.

# MQTT topic system #
Here are all the topics used in this simulation (words in uppercase are variables):
* `deliveries/assigned-transportation/DELIVERY_ID` -> `("transportation-id", TRANSPORTATION_ID)` 
* `deliveries/locations/coordinates/DELIVERY_ID` -> `("coordinates", ("latitude" : LATITUDE, "longitude" : LONGITUDE))`
* `deliveries/status/DELIVERY_ID` -> `("status", DELIVERY_STATUS)`
* `transports/health-state/TRANSPORTATION_ID` -> `("health", HEALTH_STATE)`
* `company/STORAGE_NAME/number` -> `("number", STORAGE_QUANTITY)`

# WebSocket topic system #
Application data is all non IoT-related data, parametrizer included. It obviously needs to be exchanged from the **simulator** module to angular and the other way around too. To achieve that, angular sends a topic to the **simulator**, and this latter responds with the same topic but with data attached to it. This way, the frontend knows exactly what it's receiving.

Here are all the topics used for application data:
* `company/name` -> `("name", COMPANY_NAME)`
* `company/type` -> `("company-type", COMPANY_TYPE)`
* `parametrizer/timeFlow` -> `("number", TIME_FLOW)`
* `parametrizer/dataSendingDelay` -> `("number", DATA_SENDING_DELAY)`
* `transportation/type` -> `("transportation-type", TRANSPORTATION_TYPE)`
* `set/companyName` -> `("boolean", true/false)`
* `set/timeFlow` -> `("boolean", true/false)`
* `set/dataSendingDelay`-> `("boolean", true/false)`
