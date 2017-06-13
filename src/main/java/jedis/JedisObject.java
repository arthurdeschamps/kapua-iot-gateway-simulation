package jedis;

import java.util.logging.Logger;

/**
 * Created by Arthur Deschamps on 31.05.17.
 * This interface defines the required methods to be a redis object (to be saved in db)
 */
public abstract class JedisObject {

    protected String id;

    private static final Logger logger = Logger.getLogger(JedisObject.class.getName());

    public JedisObject() {
        this.id = generateId();
    }

    // Validate that object conforms to the schema
    public abstract boolean validate();

    // Save object in db
    final boolean save() {
        return JedisManager.getInstance().save(this);
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

    @Override
    public boolean equals(Object obj) {
        try {
            JedisObject jedisObject = (JedisObject) obj;
            return (jedisObject.getId().equals(this.getId()));
        } catch (ClassCastException e) {
            e.printStackTrace();
            return false;
        }
    }
}
