package company;

import jedis.JedisManager;
import jedis.JedisObject;
import jedis.JedisObjectFactoryInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Arthur Deschamps on 01.06.17.
 * Store and manage products
 */
public class ProductStore implements JedisObjectFactoryInterface {

    private List<Product> products;

    public ProductStore() {
        products = new ArrayList<>();
    }

    @Override
    public Product retrieve(String id) {
        return null;
    }

    @Override
    public List<Product> retrieveAll() {
        // Retrieve all products from redis
        List<Product> productsJedis = JedisManager.getInstance().retrieveAllFromClass(getNewBean());
        if (productsJedis != null)
            products.addAll(productsJedis);
        return productsJedis;
    }

    @Override
    public Product getNewBean() {
        return new Product(null,null,0,0,false);
    }
}
