package company.transportation;

import jedis.JedisObjectStoreInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arthur Deschamps on 02.06.17.
 * Store and manage transportation
 */
public class TransportationStore implements JedisObjectStoreInterface<Transportation> {

    private List<Transportation> storage;

    public TransportationStore() {
        storage = new ArrayList<>();
    }


    @Override
    public Class<Transportation> getItemClass() {
        return Transportation.class;
    }

    @Override
    public void setStorage(List<Transportation> storage) {
        this.storage = storage;
    }

    @Override
    public List<Transportation> getStorage() {
        return storage;
    }
}
