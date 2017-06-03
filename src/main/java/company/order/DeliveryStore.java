package company.order;

import jedis.JedisObjectStoreInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arthur Deschamps on 03.06.17.
 */
public class DeliveryStore implements JedisObjectStoreInterface<Delivery> {

    private List<Delivery> storage;

    public DeliveryStore() {
        this.storage = new ArrayList<>();
    }

    @Override
    public Class<Delivery> getItemClass() {
        return Delivery.class;
    }

    @Override
    public void setStorage(List<Delivery> allObjects) {
        this.storage = allObjects;
    }

    @Override
    public List<Delivery> getStorage() {
        return storage;
    }
}
