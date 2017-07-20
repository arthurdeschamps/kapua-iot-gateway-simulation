package storage;

import com.google.gson.Gson;

import java.util.*;

/**
 * Defines Item factories (stores)
 *
 * @author Arthur Deschamps
 * @since 1.0
 * @see Item
 */
public class ItemStore<T extends Item> {

    private Set<T> storage;

    public ItemStore() {
        storage = new HashSet<>();
    }

    /**
     * Fill the store "storage"
     */
    public void setStorage(Set<T> storage) {
        this.storage = storage;
    }


    /**
     * Return the Store "storage"
     */
    public Set<T> getStorage() {
        return storage;
    }


    /**
     * Add object to storage and db
     */
    public void add(T object) {
        if (object != null)
            getStorage().add(object);
    }

    /**
     * Delete object from storage and db
     */
    public void delete(T object) {
        if (object != null)
            getStorage().remove(object);
    }

    /**
     * @return
     * storage as json
     */
    public String asJson() {
        Gson gson = new Gson();
        return gson.toJson(getStorage());
    }

    /**
     * @return
     * random element of the storage
     */
    public Optional<T> getRandom() {
        if (storage.size() > 0) {
            Random random = new Random();
            Iterator<T> iterator = getStorage().iterator();
            for (int i = 0; i < random.nextInt(storage.size()); i++) {
                iterator.next();
            }
            return Optional.of(iterator.next());
        }
        return Optional.empty();
    }
}
