package jedis;

/**
 * Created by Arthur Deschamps on 31.05.17.
 * This interface defines the required methods to be a redis object (to be saved in db)
 */
public abstract class JedisObject {

    protected String id;

    public JedisObject() {
        this.id = generateId();
    }

    // Validate that object conforms to the schema
    public abstract boolean validate();

    // Save object in db
    final void save() {
        JedisManager.getInstance().save(this);
    }

    // Delete object from db
    public final void delete() {
        JedisManager.getInstance().delete(this);
    }

    // Must have a unique ID
    public final String getId() {
        return id;
    }

    private String generateId() {
        return JedisManager.getInstance().generateUniqueId();
    }


}
