package websocket.server;

import org.java_websocket.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Sends data provided by the mqtt subscriptions to the frontend app via a websocket.
 * <p>The chosen mode of communication is broadcasting, that is every subscriber will receive every data.</p>
 * @since 1.0
 * @see mqtt.client.DataHandler
 * @author Arthur Deschamps
 */
class Sender {

    /**
     * List of subscribers to send the data to
     */
    private List<WebSocket> subscribers;

    private static final Logger logger = LoggerFactory.getLogger(Sender.class);

    Sender() {
        subscribers = new ArrayList<>();
    }

    void addSubscriber(WebSocket client) {
        logger.info("New subscriber");
        if (client != null)
            subscribers.add(client);
    }

    void removeSubscriber(WebSocket client) {
        logger.info("Subscriber left");
        subscribers.remove(client);
    }

    void send(String[] segments, Map<String, Object> data) {
        for (final WebSocket subscriber : subscribers)
            subscriber.send(new Response(segments,data).toString());
    }

}
