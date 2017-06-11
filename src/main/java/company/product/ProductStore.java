package company.product;

import company.product.Product;
import jedis.JedisObjectStoreInterface;

import java.util.*;

/**
 * Created by Arthur Deschamps on 01.06.17.
 * Store and manage storage
 */
public class ProductStore implements JedisObjectStoreInterface<Product> {

    private List<Product> storage;

    public ProductStore() {
        storage = new ArrayList<>();
    }

    public long getProductTypeQuantity(ProductType productType) {
        return getStorage().stream().filter(product -> product.getProductType().equals(productType)).count();
    }

    @Override
    public List<Product> getStorage() {
        return storage;
    }

    @Override
    public Class<Product> getItemClass() {
        return Product.class;
    }

    @Override
    public void setStorage(List<Product> storage) {
        this.storage = storage;
    }
}
