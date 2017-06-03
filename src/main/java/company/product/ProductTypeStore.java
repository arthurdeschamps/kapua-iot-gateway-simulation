package company.product;

import company.product.ProductType;
import jedis.JedisObjectStoreInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arthur Deschamps on 02.06.17.
 */
public class ProductTypeStore implements JedisObjectStoreInterface<ProductType> {

    private List<ProductType> productTypes;

    public ProductTypeStore() {
        productTypes = new ArrayList<>();
    }

    @Override
    public Class<ProductType> getItemClass() {
        return ProductType.class;
    }

    @Override
    public void setStorage(List<ProductType> allObjects) {
        productTypes = allObjects;
    }

    @Override
    public List<ProductType> getStorage() {
        return productTypes;
    }
}
