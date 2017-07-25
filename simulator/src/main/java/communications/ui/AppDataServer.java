package communications.ui;


import com.google.gson.Gson;
import company.company.Company;
import company.transportation.Transportation;
import company.transportation.TransportationMode;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simulation.main.Parametrizer;
import websocket.server.Response;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Sends and receive non-iot data from the UI
 * @since 1.0
 * @author Arthur Deschamps
 */
public class AppDataServer extends org.java_websocket.server.WebSocketServer {

    private Company company;
    private Parametrizer parametrizer;
    private final Gson gson = new Gson();

    private final static Logger logger = LoggerFactory.getLogger(AppDataServer.class);

    public AppDataServer(Parametrizer parametrizer, int port) {
        super(new InetSocketAddress("localhost",port));
        this.company = parametrizer.getCompany();
        this.parametrizer = parametrizer;
    }

    /**
     * Resolves a request and returns the asked result in Json format.
     * @param request
     * A request concerning non-iot data.
     * @return
     * The requested data in the form of a Json.
     */
    private Response resolve(String request) {
        String[] segments = request.split("/");
        Response result = new Response();
        Map<String, Object> data = new HashMap<>();

        // Frontend app expects to receive the same format for the request (e.g. topic/subtopic/...)
        result.setTopics(new String[]{request});

        if (segments.length == 3 && segments[0].equals("transportation") && segments[1].equals("type"))
            data.put("transportation-type", getTransportationMode(segments[2]));

        result.setData(data);
        return result;
    }
    private TransportationMode getTransportationMode(String transportationId) {
        Optional<Transportation> transportationOptional = company.getAllTransportation()
                .stream()
                .filter(transportation -> transportation.getId().equals(transportationId))
                .findFirst();

        // Required to have a copy of the enum to avoid concurrent access exception of gson conversion
        return transportationOptional.map(transportation ->
                TransportationMode.valueOf(transportation.getTransportationMode().name())).orElse(null);
    }

    private String toGson(Object obj) { return gson.toJson(obj); }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {

    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        webSocket.send(toGson(resolve(s)));
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        logger.error(e.getMessage());
        e.printStackTrace();
    }

    @Override
    public void onStart() {
        logger.info("Websocket for app data started on port " +this.getAddress().getPort()+"...");
    }
}
