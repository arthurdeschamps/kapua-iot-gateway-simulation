package communications.kapua;

import communications.kapua.gateway.KapuaGatewayClient;
import communications.kapua.subscriptions.MqttSubscriptionsManager;
import simulation.main.Parametrizer;

/**
 * Manages everything related to kapua only.
 * @since 1.0
 * @author Arthur Deschamps
 */
public class KapuaClient {

    private Parametrizer parametrizer;
    private final int port = 1883;
    private final String host = "localhost";

    public KapuaClient(Parametrizer parametrizer) {
        this.parametrizer = parametrizer;
    }

    public void start() {
        // Start sending telemetry data to kapua
        new KapuaGatewayClient(parametrizer.getCompany(),parametrizer.getDataSendingDelay(), host, port).startCommunications();
        // Start subscriptions manager
        new MqttSubscriptionsManager(host, port).startListening();
    }

}
