package communications.kapua.subscriptions;

import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.eclipse.kapua.gateway.client.Topic.ensureNotSpecial;

/**
 * Handles subscriptions.
 *
 * @author Arthur Deschamps
 */
class MqttListener implements MqttCallbackExtended {

    private static final Logger logger = LoggerFactory.getLogger(MqttListener.class);

    private IMqttAsyncClient client;
    private String clientId;
    private String applicationId;
    private String accountName;

    public MqttListener(IMqttAsyncClient client, String clientId, String applicationId, String accountName) {
        this.client = client;
        this.clientId = clientId;
        this.applicationId = applicationId;
        this.accountName = accountName;
    }

    @Override
    public void connectComplete(boolean b, String s) {
        logger.info("Connected");
        try {
            client.subscribe(topic("foo"),1);
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

    private String topic(final String... topic) {
        ensureNotSpecial(clientId);
        ensureNotSpecial(applicationId);

        return Stream.concat(
                Stream.of(accountName, clientId, applicationId),
                Arrays.stream(topic))
                .collect(Collectors.joining("/"));
    }
}
