package communications.websocket;

import company.company.Company;
import gherkin.deps.com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storage.Item;

import java.util.Optional;
import java.util.Set;

/**
 * Handles websocket client requests.
 * A request is of the form /:item-type/(all | :id)
 * @since 1.0
 * @see WebsocketServer
 * @author Arthur Deschamps
 */
class WebsocketRouter {

    private static final Logger logger = LoggerFactory.getLogger(WebsocketRouter.class);
    private final Gson gson = new Gson();
    private Company company;

    WebsocketRouter(Company company) {
        this.company = company;
    }

    /**
     * Returns requested data.
     * @param request
     * A request in the form of a route (ex: "/customers/:id", "/orders", etc)
     * @return
     * A Json of the requested data.
     */
    String handle(String request) {
        if (request.charAt(0) == '/')
            request = request.substring(1);
        final String args[] = request.split("/");
        if (args.length != 2) {
            throw new IllegalArgumentException("A request must be of the form /:item-type/(all | :id). Got: "+request);
        }

        // Parse request type
        if (args[1].equals("all"))
            return gson.toJson(getStore(args[0]));
        else
            return gson.toJson(getItem(args[0],args[1]));
    }

    /**
     * Parses the second argument of a request and returns the requested data (if the request is well formed).
     * @param itemType
     * A string that corresponds to a class extending Item (e.g. "customer","Customer","order",etc)
     * @return
     * Either an array of type @T if the request is well formed or an empty optional.
     */
    private Optional<Object[]> getStore(String itemType) {
        Optional<Set<? extends Item>> store;
        itemType = itemType.toLowerCase();

        // Finds a store (or none if itemType does not exist)
        switch (itemType) {
            case "customer":
                return Optional.of(company.getCustomers().toArray());
            case "delivery":
                return Optional.of(company.getDeliveries().toArray());
            case "order":
                return Optional.of(company.getOrders().toArray());
            case "product":
                return Optional.of(company.getProducts().toArray());
            case "product-type":
                return Optional.of(company.getProductTypes().toArray());
            case "transportation":
                return Optional.of(company.getAllTransportation().toArray());
            default:
                logger.info("No class extending Item found for string \""+itemType+"\"");
                return Optional.empty();
        }
    }

    /**
     * Tries to find an item with id @itemId and of type @itemType.
     * @param itemType
     * String corresponding to a class extending Item.
     * @param itemId
     * Id of the requested item.
     * @return
     * The item or an empty optional if not found.
     */
    private Optional<Object> getItem(String itemType, String itemId) {
        Optional<Object[]> objects = getStore(itemType);
        if (objects.isPresent()) {
            for(final Object object : objects.get()) {
                Item item;
                try {
                    item = (Item) object;
                    if (item.getId().equals(itemId))
                        return Optional.of(item);
                } catch (ClassCastException e) {
                    logger.info("Couldn't cast generic object to Item");
                    return Optional.empty();
                }
            }
        }
        return Optional.empty();
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
}
