import mqtt.client.MqttSubscriptionsManager;
import org.slf4j.LoggerFactory;
import websocket.server.WebsocketServer;

/**
 * @since 1.0
 * @author Arthur Deschamps
 */

public class Main {

    public static void main(String[] args) {

        LoggerFactory.getLogger("data-transmitter").info("Opening websocket...");
        final int wsPort = 8054;
        WebsocketServer wsServer = new WebsocketServer(wsPort);
        wsServer.start();

        final int mqttPort = 1883;
        final String host = "localhost";
        new MqttSubscriptionsManager(host,mqttPort, wsServer).startListening();
    }
}
