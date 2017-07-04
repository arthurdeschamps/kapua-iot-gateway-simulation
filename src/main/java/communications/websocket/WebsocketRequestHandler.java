package communications.websocket;

import company.company.Company;
import gherkin.deps.com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storage.Item;

import java.util.Optional;

/**
 * Handles websocket client requests.
 * A request is of the form /:item-type/(all | :id)
 * @since 1.0
 * @see WebsocketServer
 * @author Arthur Deschamps
 */
class WebsocketRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebsocketRequestHandler.class);
    private final Gson gson = new Gson();
    private Company company;

    WebsocketRequestHandler(Company company) {
        this.company = company;
    }

    /**
     * Returns requested data.
     * @param jsonRequest
     * A string describing the element(s) to retrieve.
     * @return
     * A Json of the requested data.
     */
    Optional<String> handle(String jsonRequest) {
        Request request = getRequest(jsonRequest);
        Response response = new Response();

        // Check if conversion failed
        if (request == null || request.getRequests() == null || request.getRequestType() == null)
            return Optional.empty();

        switch (request.getRequestType()) {

            case ALL:
                if (request.getRequests().length == 1) {
                    if (request.getRequests()[0].equals("company"))
                        response = new Response("company",getAll("company"));
                }

            case ONE:
                if (request.getRequests().length == 1) {
                    response = getCompanyResource(request.getRequests()[0]);
                }

            case SUBSCRIBE:
                if (request.getRequests().length == 1) {
                    subscribe(request.getRequests()[0]);
                }
        }

        return response != null ? Optional.of(gson.toJson(response)) : Optional.empty();
    }

    private void subscribe(String topic) {
        //TODO
    }

    /**
     * Sends every company data.
     * @return
     * All data related to the company.
     */
    private Response getAll(String topic) {
        if (topic.equals("company"))
            return new Response("company",gson.toJson(company));
        return null;
    }

    /**
     * Handles company data requests.
     * @param topic
     * What type of data concerning the company.
     * @return
     * Either a company resource or an empty optional if the request is malformed.
     */
    private Response getCompanyResource(String topic) {
        if (topic.equals("headquarters"))
            return new Response("headquarters",company.getHeadquarters().getCoordinates());
        return null;
    }

    /**
     * Parses the second argument of a request and returns the requested data (if the request is well formed).
     * @param itemType
     * A string that corresponds to a class extending Item (e.g. "customer","Customer","order",etc)
     * @return
     * Either an array of type @T if the request is well formed or an empty optional.
     */
    private Response getStore(String itemType) {
        itemType = itemType.toLowerCase();

        // Finds a store (or none if itemType does not exist)
        switch (itemType) {
            case "customer":
                return new Response("customers",company.getCustomers());
            case "delivery":
                return new Response("deliveries",company.getDeliveries());
            case "order":
                return new Response("orders",company.getOrders());
            case "product":
                return new Response("products",company.getProducts());
            case "product-type":
                return new Response("productTypes",company.getProductTypes());
            case "transportation":
                return new Response("allTransportation",company.getAllTransportation());
            default:
                logger.info("No class extending Item found for string \""+itemType+"\"");
                return null;
        }
    }

    /**
     * Parses the first argument of a request, which is supposedly a class extending Item.
     * @param word
     * A string which is supposedly a class extending item.
     * @return
     * If @word is a class extending Item, then it returns the corresponding class. Otherwise, it returns an empty
     * optional.
     */
    private Optional<Class> getItemType(String word) {
        try {
            Class clazz = Class.forName(word);
            if (Item.class.isAssignableFrom(clazz))
                return Optional.of(clazz);
            else
                return Optional.empty();
        } catch (ClassNotFoundException e) {
            logger.info("No class extending Item found for \""+word+"\"");
            return Optional.empty();
        }
    }

    private Request getRequest(String jsonRequest) {
        return gson.fromJson(jsonRequest,Request.class);
    }
}
