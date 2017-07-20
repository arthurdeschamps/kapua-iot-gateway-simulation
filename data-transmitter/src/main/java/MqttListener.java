import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Handler on received messages from subscriptions
 * @author Arthur Deschamps
 */
class MqttListener implements MqttCallbackExtended {

    private static final Logger logger = LoggerFactory.getLogger(MqttListener.class);

    private IMqttAsyncClient client;
    private String clientId;
    private String publisherId;
    private String applicationId;
    private String publisherAccountName;

    public MqttListener(IMqttAsyncClient client, String clientId, String publisherId, String applicationId, String publisherAccountName) {
        this.client = client;
        this.clientId = clientId;
        this.publisherId = publisherId;
        this.applicationId = applicationId;
        this.publisherAccountName = publisherAccountName;
    }

    @Override
    public void connectComplete(boolean b, String s) {
        logger.info("Connected");
        try {
            logger.info(topic("foo"));
            client.subscribe(topic("foo"),1);
            logger.info("Subscriptions started");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        logger.info("Subscriptions stopped");
        logger.info("Reason: "+throwable.getMessage());
        throwable.printStackTrace();
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        // s is the topic
        logger.info(s);
        logger.info(mqttMessage.getPayload().toString();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        try {
            logger.info(iMqttDeliveryToken.getMessage().toString());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private String topic(final String... topic) {
        return Stream.concat(
                Stream.of(publisherAccountName,publisherId, applicationId),
                Arrays.stream(topic))
                .collect(Collectors.joining("/"));
    }
}
