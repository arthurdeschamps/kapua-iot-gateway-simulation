package company.product;

import company.product.ProductType;
import jedis.JedisObjectStoreInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arthur Deschamps on 02.06.17.
 */
public class ProductTypeStore implements JedisObjectStoreInterface<ProductType> {

    private List<ProductType> storage;

    public ProductTypeStore() {
        storage = new ArrayList<>();
    }

    @Override
    public Class<ProductType> getItemClass() {
        return ProductType.class;
    }

    @Override
    public void setStorage(List<ProductType> allObjects) {
        storage = allObjects;
    }

    @Override
    public List<ProductType> getStorage() {
        return storage;
    }
}
