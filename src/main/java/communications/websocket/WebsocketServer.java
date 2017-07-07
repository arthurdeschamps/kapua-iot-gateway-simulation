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

    private WebsocketRequestHandler requestHandler;
    private final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    public WebsocketServer(Company company, int port) {
        super(new InetSocketAddress(port));
        this.requestHandler = new WebsocketRequestHandler(company);
    }

    public WebsocketServer(Company company, InetSocketAddress address) {
        super(address);
        this.requestHandler = new WebsocketRequestHandler(company);
    }

    public WebsocketServer(Company company) {
        super();
        this.requestHandler = new WebsocketRequestHandler(company);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        logger.info("New client");
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        logger.info("Client socket closed");
    }

    @Override
    public void onMessage(WebSocket webSocket, String jsonRequest) {
        logger.info(jsonRequest);
        requestHandler.handle(jsonRequest).ifPresent((String data) -> {
                if (webSocket != null) webSocket.send(data);
        });
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
