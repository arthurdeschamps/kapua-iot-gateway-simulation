package communications.kapua;

import simulation.main.Parametrizer;

/**
 * Manages everything related to kapua only.
 * @since 1.0
 * @author Arthur Deschamps
 */
public class KapuaClient {

    private Parametrizer parametrizer;
    private String host;
    private int port;

    public KapuaClient(Parametrizer parametrizer, String host, int port) {
        this.parametrizer = parametrizer;
        this.host = host;
        this.port = port;
    }

    public void start() {
        // Start sending telemetry data to kapua
        new KapuaGatewayClient(parametrizer.getCompany(),parametrizer.getDataSendingDelay()).startCommunications();
        // Start subscriptions manager
        new MqttSubscriptionsManager(host, port).startListening();
    }

}
