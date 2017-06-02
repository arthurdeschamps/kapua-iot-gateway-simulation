package company;

import jedis.JedisObjectStoreInterface;

import java.util.*;

/**
 * Created by Arthur Deschamps on 01.06.17.
 * Store and manage storage
 */
public class ProductStore implements JedisObjectStoreInterface<Product> {

    private List<Product> storage;
    private Map<String, Integer> productsQuantities;

    public ProductStore() {
        storage = new ArrayList<>();
        productsQuantities = new HashMap<>();
    }

    @Override
    public Product getNewBean() {
        return new Product(null,null,0,0,false);
    }

    public Map<String, Integer> getProductsQuantities() {
        return productsQuantities;
    }

    public void setProductsQuantities(Map<String, Integer> productsQuantities) {
        this.productsQuantities = productsQuantities;
    }

    @Override
    public List<Product> getStorage() {
        return storage;
    }

    @Override
    public void setStorage(List<Product> storage) {
        this.storage = storage;
    }
}
