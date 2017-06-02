package company;

import jedis.JedisManager;
import jedis.JedisObjectStoreInterface;

import java.util.*;

/**
 * Created by Arthur Deschamps on 01.06.17.
 * Store and manage products
 */
public class ProductStore implements JedisObjectStoreInterface<Product> {

    private List<Product> products;
    private Map<String, Integer> productsQuantities;

    public ProductStore() {
        products = new ArrayList<>();
        productsQuantities = new HashMap<>();
    }

    @Override
    public Optional<Product> retrieve(String productName) {
        return getProducts().stream().findFirst().filter(product -> product.getName().equals(productName));
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

    public int getProductQuantityRemaining(String productId) {
        return getProductsQuantities().get(productId);
    }

    public void setProductQuantityRemaining(String productId, int productQuantity) {
        getProductsQuantities().put(productId, productQuantity);
    }

    public Map<String, Integer> getProductsQuantities() {
        return productsQuantities;
    }

    public void setProductsQuantities(Map<String, Integer> productsQuantities) {
        this.productsQuantities = productsQuantities;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
