package storage;

import gherkin.deps.com.google.gson.Gson;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * Created by Arthur Deschamps on 31.05.17.
 * This interface defines the required methods to be a redis object (to be saved in db)
 */
public abstract class Item {

    protected String id;

    private static final Logger logger = Logger.getLogger(Item.class.getName());

    public Item() {
        this.id = generateId();
    }

    // Validate that object conforms to the schema
    public abstract boolean validate();

    // Must have a unique ID
    public final String getId() {
        return id;
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object obj) {
        try {
            Item item = (Item) obj;
            return (item.getId().equals(this.getId()));
        } catch (ClassCastException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String asJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
