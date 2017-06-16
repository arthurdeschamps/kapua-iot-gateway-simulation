package storage;

import gherkin.deps.com.google.gson.Gson;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * Created by Arthur Deschamps on 31.05.17.
 * This interface defines the required methods to be a redis object (to be saved in db)
 */
public abstract class Item {

    private static final Logger logger = Logger.getLogger(Item.class.getName());

    // Validate that object conforms to the schema
    public abstract boolean validate();


    private String generateId() {
        return UUID.randomUUID().toString();
    }


    public String asJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
