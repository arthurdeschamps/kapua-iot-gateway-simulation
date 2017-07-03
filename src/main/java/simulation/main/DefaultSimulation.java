package simulation.main;

import communications.kapua.KapuaGatewayClient;
import communications.websocket.WebsocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simulation.simulators.SupplyChainControlSimulator;

/**
 * Default simulation. Runs economy, company and delivery transport simulations and sends data to kapua.
 * @author Arthur Deschamps
 * @since 1.0
 */
public class DefaultSimulation {

    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(DefaultSimulation.class);

        logger.info("Creating parametrizer...");
        // Uncomment the two lines below and modify the values at will to parametrize the simulation
        // Parametrizer parametrizer = new Parametrizer(100, 3, false, 0,
        //         CompanyGenerator.generateInternationalCompany(), false, null);

        // Comment the line below if you don't want the default parameters for the simulation
        Parametrizer parametrizer = new Parametrizer();

        // Or use the default parametrizer and set the things you want:
        parametrizer.setDisplayMetrics(false);

        // Starts the simulation
        logger.info("Starting simulators...");
        new SupplyChainControlSimulator(parametrizer).start();

        logger.info("Opening websocket...");
        WebsocketServer socket = new WebsocketServer(parametrizer.getCompany(),8054);
        socket.start();

        // Start sending data and subscribing
        logger.info("Initializing communication with Kapua...");
        new KapuaGatewayClient(parametrizer.getCompany(),parametrizer.getDataSendingDelay()).startCommunications();
    }

}
