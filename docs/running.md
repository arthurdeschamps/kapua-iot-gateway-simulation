
Running the simulation
=======

Running the **Kapua** services
-------
If you haven't already, take a look at [these instructions](https://github.com/eclipse/kapua/tree/develop/assembly).
You will need at least 3 Kapua services started in order to run the simulation:

 1. kapua-broker
 2. kapua-sql
 3. kapua-elasticsearch

If you have the Kapua docker containers created on your computer, you can start them with:

    docker start kapua-sql
    docker start kapua-elasticsearch
    docker start kapua-broker


Running **simulator** and **data-transmitter**
-----------

 - **The maven way**:
 You will need Maven installed. See [these](https://maven.apache.org/install.html) instructions.
 Go to **simulator** and then **data-transmitter** and run in both directories:
 
 		$ mvn install   # Downloads dependencies and compiles everything
 		$ mvn exec:java # Run
	
 	That's it !
    
 - **The IDE way**:
  If you have access to **IntelliJ idea** (or similar), you can import the global project in the IDE and simply run the two modules **simulator** and **data-transmitter** (no matter the order). No specific option is required, just run the main class for both module.
  
 - **The JAR way**:
 Go to directory **simulator/target** and run:

        $ java -jar simulator-1.0-SNAPSHOT-jar-with-dependencies.jar

    Go to directory **data-transmitter/target** and run:
 
        $ java -jar data-transmitter-1.0-SNAPSHOT-jar-with-dependencies.jar 
	
    You can also build the jars by running 
    
    	$ mvn package
	
    in the module's directory you desire.


Running the **web application**
-------
- **The easy way**:
Go to **webapp-angular/build/web/** and open index.html in any browser you desire.

- **The pub way**:
 
	 1. Install the **pub** tool. Follow this [link](https://www.dartlang.org/tools/pub/installing) for further instructions.

	 2. Then, if you don't already have it, install [Dartium](https://webdev.dartlang.org/tools/dartium)  (Chromium with the Dart VM). 
	 3. Thereafter, run in **webapp-angular** directory:
 
	        $ pub serve --port 8082
	and open localhost:8082 in Dartium.
	
	*Note that you can choose another port, but neither 8080 or 8081 will be available if you have **kapua-console** and **kapua-api** running on their default ports.*

- **The IDE way**:
Using **WebStorm** you can simply run the built-in configuration called "index.html".
If you encounter 404 errors, just change the port of the configuration. For instance change it from 63342 to 63343.
