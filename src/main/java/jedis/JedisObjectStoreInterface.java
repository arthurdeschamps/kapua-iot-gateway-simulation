package jedis;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Created by Arthur Deschamps on 01.06.17.
 * Defines JedisObject factories (stores)
 */
public interface JedisObjectStoreInterface<T extends JedisObject> {

    /**
     * Retrieves an object by id
     */
    default Optional<T> retrieve(String id) {
        return JedisManager.getInstance().retrieve(id,this.getItemClass());
    }

    /**
     * Retrieves all objects of a give type from the database
     */
    default List<T> retrieveAll() {
        return JedisManager.getInstance().retrieveAllFromClass(getItemClass());
    }


    /**
     * Return stored items' class
     */
    Class<T> getItemClass();

    /**
     * Fill the store "storage"
     */
    void setStorage(List<T> allObjects);


    /**
     * Return the Store "storage"
     */
    List<T> getStorage();


    /**
     * Add object to storage and db
     */
    default void add(T object) {
        if (object != null)
            if (!JedisManager.getInstance().retrieve(object.getId(), object.getClass()).isPresent())
                if (getStorage().add(object))
                    object.save();
    }

    /**
     * Delete object from storage and db
     */
    default void delete(T object) {
        if (object != null)
            if (getStorage().remove(object))
                object.delete();
    }

}
