package storage;

import gherkin.deps.com.google.gson.Gson;

import java.util.UUID;

/**
 * Defines an Item. A item is stored in an ItemStore. It can be for example a Product, a Order, etc.
 * @author Arthur Deschamps
 * @since 1.0
 * @see ItemStore
 */
public abstract class Item {

    private String id = UUID.randomUUID().toString();

    /**
     * Validate that object conforms to the schema
     * @return
     * True if the object is validated. False otherwise.
     */
    public abstract boolean validate();

    /**
     * Converts the item to a Json object.
     * @return
     * The item converted into Json format.
     */
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    /**
     * Returns an id unique to each item.
     * @return
     * The id.
     */
    public String getId() {
        return id;
    }
}
