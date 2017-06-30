package communications.websocket;

import company.company.Company;
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
public class WebsocketServer extends WebSocketServer {

    private WebsocketRouter router;
    private final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    public WebsocketServer(Company company, int port) {
        super(new InetSocketAddress(port));
        this.router = new WebsocketRouter(company);
    }

    public WebsocketServer(Company company, InetSocketAddress address) {
        super(address);
        this.router = new WebsocketRouter(company);
    }

    public WebsocketServer(Company company) {
        super();
        this.router = new WebsocketRouter(company);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        String response = router.handle(clientHandshake.getResourceDescriptor());
        webSocket.send(response);
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        logger.info("Client socket closed");
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
