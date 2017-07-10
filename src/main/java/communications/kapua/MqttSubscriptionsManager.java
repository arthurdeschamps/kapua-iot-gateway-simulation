package communications.kapua;

import org.fusesource.mqtt.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;

/**
 * Mqtt client wrapper. This class only deals manages subscriptions.
 * @since 1.0
 * @see DataSenderRunner
 * @see KapuaGatewayClient
 * @author Arthur Deschamps
 */
class MqttSubscriptionsManager {

    private String host;
    private int port;

    private static final Logger logger = LoggerFactory.getLogger(MqttSubscriptionsManager.class);

    MqttSubscriptionsManager(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Starts listening the gateway to Kapua and subscribes to every data.
     */
    void startListening() {
        MQTT mqtt = new MQTT();
        try {
            mqtt.setHost(host,port);
            mqtt.setClientId("supply-chain-control-simulator-listener");
            mqtt.setUserName("kapua-broker");
            mqtt.setPassword("kapua-password");
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }
        final CallbackConnection connection = mqtt.callbackConnection();
        connection.listener(new MqttSubscriptionsListener(connection));
        connection.connect(new Callback<Void>() {
            @Override
            public void onSuccess(Void value) {
                Topic[] allTopics = {new Topic("deliveries/locations/#",QoS.AT_LEAST_ONCE)};
                connection.subscribe(allTopics, new Callback<byte[]>() {
                    @Override
                    public void onSuccess(byte[] value) {
                        logger.info("Subscriptions started");
                    }

                    @Override
                    public void onFailure(Throwable value) {
                        logger.error(value.getMessage());
                        value.printStackTrace();
                    }
                });
            }

            @Override
            public void onFailure(Throwable value) {
                logger.error(value.getMessage());
                value.printStackTrace();
            }
        });
    }

}
