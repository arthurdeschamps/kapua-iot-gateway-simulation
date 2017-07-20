import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles subscriptions to Kapua topics.
 * @since 1.0
 * @author Arthur Deschamps
 */
public class MqttSubscriptionsManager {

    private String broker;

    private static final Logger logger = LoggerFactory.getLogger(MqttSubscriptionsManager.class);

    public MqttSubscriptionsManager(final String host, final int port) {
        this.broker = "tcp://"+host+":"+Integer.toString(port);
    }

    /**
     * Starts listening the gateway to Kapua and subscribes to every data.
     */
    public void startListening() {
        try {
            final String clientId = "listener";
            IMqttAsyncClient client = new MqttAsyncClient(broker, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName("kapua-sys");
            connOpts.setPassword("kapua-password".toCharArray());
            connOpts.setCleanSession(true);
            logger.info("Connecting to broker: "+broker);
            client.setCallback(new MqttListener(client,clientId,"supply-chain-control-simulation",
                    "kapua-iot-gateway-simulation-scm","kapua-sys"));
            client.connect(connOpts);
        } catch(MqttException me) {
            logger.error("Reason: "+me.getReasonCode());
            logger.error("Message: "+me.getMessage());
            logger.error("Location: "+me.getLocalizedMessage());
            logger.error("Cause: "+me.getCause());
            logger.error("Exception: "+me);
            me.printStackTrace();
        }

    }
}
