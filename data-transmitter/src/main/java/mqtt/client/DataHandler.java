package mqtt.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import websocket.server.WebsocketServer;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Handles maps received through the subscription mechanism.
 * @since 1.0
 * @author Arthur Deschamps
 */
public class DataHandler {

    private static final Logger logger = LoggerFactory.getLogger(DataHandler.class);

    private String applicationId;
    private String publisherId;
    private String publisherAccountName;
    private WebsocketServer wsServer;

    DataHandler(String applicationId, String publisherId, String publisherAccountName, WebsocketServer wsServer) {
        this.applicationId = applicationId;
        this.publisherId = publisherId;
        this.publisherAccountName = publisherAccountName;
        this.wsServer = wsServer;
    }

    void handle(String topic, Map<String, Object> data) {
        if (data == null || topic == null)
            return;

        String[] segments = getSegments(topic);
        wsServer.send(segments, data);
    }

    /**
     * Retains only the topic itself (without for instance the application id) and splits it into multiple segments.
     * @param topic
     * A topic received from kapua.
     * @return
     * A list of strings representing the main and sub topics.
     */
    private String[] getSegments(String topic) {
        logger.info(topic);
        final String kapuaMeta = Stream.of(publisherAccountName, publisherId, applicationId).collect(Collectors.joining("/"))+"/";
        return topic.replace(kapuaMeta,"").split("/");
    }
}
