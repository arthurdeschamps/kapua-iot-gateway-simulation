/**
 * Created by Arthur Deschamps on 30.05.17.
 */

import company.Product;
import company.ProductStore;
import jedis.JedisManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CompanyTest {

    private List<Product> products;
    private ProductStore productStore;

    @Before
    public void setUp() throws IOException {
        //TODO test database server (to not interfere in app db)
        productStore = new ProductStore();

        products = new ArrayList<>();
        products.add(new Product("Basket Ball","USA",20.0f,0.6f,false));
        // Create a product with a identical id (name). Should override elder values.
        products.add(new Product("Basket Ball","France",20.0f,2.0f,false));
        products.add(new Product("Swiss watch","Switzerland",3450.0f,0.567f,true));
    }

    @After
    public void tearDown() {
        for (final Product product : products)
            product.delete();
    }

    @Test
    public void products() {
        for (final Product product : products)
            product.save();

        Product watch = new Product("Swiss watch","Switzerland",3450.0f,0.567f,true);
        Product result = JedisManager.getInstance().retrieve(watch.getId(),productStore.getNewBean());
        Assert.assertNotNull(result);
        Assert.assertEquals(watch.getName(),result.getName());
        //Assert.assertEquals(watch.getProductionCountry(),result.getProductionCountry());
        Assert.assertEquals(watch.isFragile(),result.isFragile());
        Assert.assertTrue(watch.getWeight() == result.getWeight());


        List<Product> newProducts = JedisManager.getInstance().retrieveAllFromClass(productStore.getNewBean());
        newProducts.removeIf(product -> !(product.getName().equals("Basket Ball")));
        Assert.assertEquals(newProducts.size(),1);
        Assert.assertEquals(newProducts.get(0).getProductionCountry(),"France");
    }
}
