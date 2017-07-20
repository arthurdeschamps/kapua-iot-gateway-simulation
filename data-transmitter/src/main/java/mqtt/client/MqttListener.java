package mqtt.client;

import org.eclipse.kapua.gateway.client.Payload;
import org.eclipse.kapua.gateway.client.kura.internal.Metrics;
import org.eclipse.kapua.gateway.client.kura.payload.KuraPayloadProto.KuraPayload;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import websocket.server.WebsocketServer;

import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Mqtt server callback handler.
 * @author Arthur Deschamps
 */
class MqttListener implements MqttCallbackExtended {

    private static final Logger logger = LoggerFactory.getLogger(MqttListener.class);

    private IMqttAsyncClient client;
    private String publisherId;
    private String applicationId;
    private String publisherAccountName;
    private DataHandler dataHandler;

    MqttListener(IMqttAsyncClient client, String publisherId, String applicationId,
                        String publisherAccountName, WebsocketServer wsServer) {
        this.client = client;
        this.publisherId = publisherId;
        this.applicationId = applicationId;
        this.publisherAccountName = publisherAccountName;
        this.dataHandler = new DataHandler(applicationId,publisherId,publisherAccountName,wsServer);
    }

    @Override
    public void connectComplete(boolean b, String s) {
        logger.info("Connected");
        subscribeAll();
    }

    @Override
    public void connectionLost(Throwable throwable) {
        logger.info("Subscriptions stopped");
        logger.info("Reason: "+throwable.getMessage());
        throwable.printStackTrace();
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        // Decode the key-value pair
        dataHandler.handle(s, decode(mqttMessage.getPayload()).getValues());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        try {
            logger.info(iMqttDeliveryToken.getMessage().toString());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * Subscribes to every topic.
     */
    private void subscribeAll() {
        try {
            client.subscribe(topic("#"),1);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        logger.info("Subscriptions started");
    }

    /**
     * Assembles a proper topic for kapua.
     * @param topic
     * The raw topic, for instance ["foo"] for topic "foo", ["foo", "bar"] for topic "foo/bar"
     * @return
     * A well formed topic that can be used to subscribe to the kapua broker.
     */
    private String topic(final String... topic) {
        return Stream.concat(
                Stream.of(publisherAccountName,publisherId, applicationId),
                Arrays.stream(topic))
                .collect(Collectors.joining("/"));
    }

    /**
     * Decodes an MqttMessage payload sent by the kapua broker.
     * @param data
     * The received message payload.
     * @return
     * A map containing one or multiple key-value pairs.
     * @throws Exception
     * KuraPayload.parseFrom() might potentially raise an exception.
     */
    private Payload decode(final byte[] data) throws Exception {
        Objects.requireNonNull(data);

        final KuraPayload payload = KuraPayload.parseFrom(data);
        final Map<String, Object> values = Metrics.extractMetrics(payload);
        return Payload.of(Instant.ofEpochMilli(payload.getTimestamp()), values);
    }
}
