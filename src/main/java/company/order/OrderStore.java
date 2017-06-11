package company.order;

import company.customer.Customer;
import jedis.JedisObjectStoreInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Arthur Deschamps on 03.06.17.
 */
public class OrderStore implements JedisObjectStoreInterface<Order> {

    private List<Order> storage;

    public OrderStore() {
        storage = new ArrayList<>();
    }

    public Order getByCustomer(Customer customer) {
        Optional<Order> candidate = getStorage().stream().filter(order -> order.getBuyer().equals(customer)).findFirst();
        return candidate.orElse(null);
    }

    @Override
    public Class<Order> getItemClass() {
        return Order.class;
    }

    @Override
    public void setStorage(List<Order> allObjects) {
        this.storage = allObjects;
    }

    @Override
    public List<Order> getStorage() {
        return storage;
    }
}
