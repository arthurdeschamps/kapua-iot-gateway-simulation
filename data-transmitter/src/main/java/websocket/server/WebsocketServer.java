package websocket.server;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;

/**
 * Communicates with the frontend app
 * @since 1.0
 * @author Arthur Deschamps
 */
public class WebsocketServer extends WebSocketServer {

    private final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    private final Sender sender = new Sender();

    public WebsocketServer(int port) {
        super(new InetSocketAddress(port));
    }

    public void send(String[] segments, Map<String, Object> data) {
        sender.send(segments,data);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        sender.addSubscriber(webSocket);
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        sender.removeSubscriber(webSocket);
    }

    @Override
    public void onMessage(WebSocket webSocket, String jsonRequest) {
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        logger.error(e.getMessage());
        e.printStackTrace();
    }

    @Override
    public void onStart() {
        logger.info("Websocket started on port " + +this.getAddress().getPort()+"...");
    }
}
