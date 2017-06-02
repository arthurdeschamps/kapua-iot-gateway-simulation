package company;

import jedis.JedisManager;
import jedis.JedisObjectStoreInterface;
import redis.clients.jedis.GeoCoordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public Transportation getNewBean() {
        return new Transportation(0,new GeoCoordinate(0,0),0,null);
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
