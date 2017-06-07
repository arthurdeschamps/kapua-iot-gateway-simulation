package company.customer;

import com.github.javafaker.Faker;
import jedis.JedisObjectStoreInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Arthur Deschamps on 03.06.17.
 */
public class CustomerStore implements JedisObjectStoreInterface<Customer> {

    private List<Customer> storage;

    public CustomerStore() {
        this.storage = new ArrayList<>();
    }

    public void removeRandomCustomer() {
        Random random = new Random();
        this.getStorage().remove(random.nextInt(this.getStorage().size()));
    }

    @Override
    public Class<Customer> getItemClass() {
        return Customer.class;
    }

    @Override
    public void setStorage(List<Customer> allObjects) {
        this.storage = allObjects;
    }

    @Override
    public List<Customer> getStorage() {
        return storage;
    }
}
