package communications.kapua.subscriptions;

import communications.kapua.gateway.DataSenderRunner;
import communications.kapua.gateway.KapuaGatewayClient;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
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
    private final String clientId = "supply-chain-control-simulation-listener";
    private String broker;
    private final String username = "kapua-sys";
    private final String password = "kapua-password";
    private String applicationId;
    private String accountName;

    private static final Logger logger = LoggerFactory.getLogger(MqttSubscriptionsManager.class);

    public MqttSubscriptionsManager(final String accountName, final String applicationId, final String host, final int port) {
        this.accountName = accountName;
        this.applicationId = applicationId;
        this.host = host;
        this.port = port;
        this.broker = "tcp://"+host+":"+Integer.toString(port);
    }

    /**
     * Starts listening the gateway to Kapua and subscribes to every data.
     */
    public void startListening() {
        try {
            IMqttAsyncClient client = new MqttAsyncClient(broker, clientId);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName(username);
            connOpts.setPassword(password.toCharArray());
            connOpts.setCleanSession(true);
            logger.info("Connecting to broker: "+broker);
            client.setCallback(new MqttListener(client,clientId,applicationId,accountName));
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
