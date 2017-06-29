package communications.websocket;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Communicates with the frontend app
 * @since 1.0
 * @author Arthur Deschamps
 */
public class WebApplicationDataSender extends WebSocketServer {

    private final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    public WebApplicationDataSender(int port) {
        super(new InetSocketAddress(port));
    }

    public WebApplicationDataSender(InetSocketAddress address) {
        super(address);
    }

    public WebApplicationDataSender() {
        super();
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        logger.info("New connection: "+clientHandshake.getResourceDescriptor());
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        logger.info("Websocket closed");
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        logger.info(s);
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
