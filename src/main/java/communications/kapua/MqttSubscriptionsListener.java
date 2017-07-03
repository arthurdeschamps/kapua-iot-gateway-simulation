package communications.kapua;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.ExtendedListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles
 * @since 1.0
 * @see MqttSubscriptionsManager
 * @author Arthur Deschamps
 */
public class MqttSubscriptionsListener implements ExtendedListener {

    private CallbackConnection connection;
    private static final Logger logger = LoggerFactory.getLogger(MqttSubscriptionsListener.class);

    public MqttSubscriptionsListener(CallbackConnection connection) {
        this.connection = connection;
    }

    @Override
    public void onPublish(UTF8Buffer topic, Buffer body, Callback<Callback<Void>> ack) {

    }

    @Override
    public void onConnected() {
    }

    @Override
    public void onDisconnected() {
        logger.info("Subscriptions stopped");
        connection.disconnect(null);
    }

    @Override
    public void onPublish(UTF8Buffer topic, Buffer body, Runnable ack) {
        logger.info("Message published. Topic: "+topic.toString()+". Body: "+body.toString());
        ack.run();
    }

    @Override
    public void onFailure(Throwable value) {
        logger.error(value.getMessage());
    }
}
