package jedis;

/**
 * Created by Arthur Deschamps on 31.05.17.
 * This interface defines the required methods to be a redis object (to be saved in db)
 */
public abstract class JedisObject {

    // Validate that object conforms to the schema
    public abstract boolean validate();

    // Save object in db
    void save() {
        JedisManager.getInstance().save(this);
    }

    // Delete object from db
    public void delete() {
        JedisManager.getInstance().delete(this);
    }

    // Must have a unique ID
    public abstract String getId();


}
