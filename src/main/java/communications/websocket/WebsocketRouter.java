package communications.websocket;

import gherkin.deps.com.google.gson.Gson;

/**
 * Handles websocket client requests.
 * @since 1.0
 * @see WebsocketServer
 * @author Arthur Deschamps
 */
public class WebsocketRouter {

    /**
     * Returns requested data.
     * @param request
     * A request in the form of a route (ex: "/customers/:id", "/orders", etc)
     * @return
     * A Json of the requested data.
     */
    public Gson get(String request) {

        return null;
    }

}
