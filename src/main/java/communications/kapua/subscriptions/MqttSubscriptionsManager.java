package communications.kapua.subscriptions;

import communications.kapua.gateway.DataSenderRunner;
import communications.kapua.gateway.KapuaGatewayClient;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mqtt client wrapper. This class only deals manages subscriptions.
 * @since 1.0
 * @see DataSenderRunner
 * @see KapuaGatewayClient
 * @author Arthur Deschamps
 */
public class MqttSubscriptionsManager {

    private String host;
    private int port;

    private static final Logger logger = LoggerFactory.getLogger(MqttSubscriptionsManager.class);

    public MqttSubscriptionsManager(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Starts listening the gateway to Kapua and subscribes to every data.
     */
    public void startListening() {
        String topic = "foo";
        String broker = "tcp://"+host+":"+Integer.toString(port);
        String clientId = "supply-chain-control-simulation-listener";
        String username = "kapua-broker";
        String password = "kapua-password";

        try {
            IMqttAsyncClient client = new MqttAsyncClient(broker, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName(username);
            connOpts.setPassword(password.toCharArray());
            connOpts.setCleanSession(true);
            logger.info("Connecting to broker: "+broker);
            client.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean b, String s) {
                    logger.info("Connected");
                    try {
                        client.subscribe(topic,2);
                        logger.info("Subscriptions started");
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void connectionLost(Throwable throwable) {
                    logger.info("Subscriptions stopped");
                }

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    logger.info(s);
                    logger.info(mqttMessage.getPayload().toString());
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                }
            });
            client.connect(connOpts);
        } catch(MqttException me) {
            logger.error("reason "+me.getReasonCode());
            logger.error("msg "+me.getMessage());
            logger.error("loc "+me.getLocalizedMessage());
            logger.error("cause "+me.getCause());
            logger.error("excep "+me);
            me.printStackTrace();
        }

    }
}
