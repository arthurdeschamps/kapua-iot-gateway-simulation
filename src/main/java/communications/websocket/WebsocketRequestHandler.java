package communications.websocket;

import company.address.Coordinates;
import company.company.Company;
import gherkin.deps.com.google.gson.Gson;
import org.apache.commons.lang3.SerializationUtils;
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
        if (request == null || request.getTopics() == null || request.getRequestType() == null)
            return Optional.empty();

        response.setTopics(request.getTopics());

        String[] topics = request.getTopics();

        switch (request.getRequestType()) {

            case ALL:
                if (topics.length == 1) {
                    if (topics[0].equals("company"))
                        response.setData(getAll("company"));
                }
                break;

            case MULTIPLE:
                if (topics.length == 2 && topics[0].equals("company")) {
                    response.setData(getStore(topics[1]));
                }
                break;

            case ONE:
                if (request.getTopics().length == 2) {
                    String[] requests = request.getTopics();
                    if (requests[0].equals("company"))
                        response.setData(getCompanyResource(requests[1]));
                }
                break;

        }

        return response.getData() != null ? Optional.of(gson.toJson(response)) : Optional.empty();
    }

    /**
     * Sends every company data.
     * @return
     * All data related to the company.
     */
    private Object getAll(String topic) {
        if (topic.equals("company")) return SerializationUtils.clone(company);
        return null;
    }

    /**
     * Handles company data requests.
     * @param topic
     * What type of data concerning the company.
     * @return
     * Either a company resource or an empty optional if the request is malformed.
     */
    private Object getCompanyResource(String topic) {
        if (topic.equals("headquarters"))
            return new Coordinates(company.getHeadquarters().getCoordinates());
        return null;
    }

    /**
     * Parses the second argument of a request and returns the requested data (if the request is well formed).
     * @param itemType
     * A string that corresponds to a class extending Item (e.g. "customer","Customer","order",etc)
     * @return
     * Either an array of type @T if the request is well formed or an empty optional.
     */
    private Object getStore(String itemType) {
        itemType = itemType.toLowerCase();

        Object store;

        // Finds a store (or none if itemType does not exist)
        switch (itemType) {
            case "customer":
                store = company.getCustomers().toArray();
                break;
            case "delivery":
                store = company.getDeliveries().toArray();
                break;
            case "order":
                store = company.getOrders().toArray();
                break;
            case "product":
                store = company.getProducts().toArray();
                break;
            case "product-type":
                store = company.getProductTypes().toArray();
                break;
            case "transportation":
                store = company.getAllTransportation().toArray();
                break;
            default:
                store = null;
                logger.info("No class extending Item found for string \""+itemType+"\"");
                break;
        }

        return store;
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
