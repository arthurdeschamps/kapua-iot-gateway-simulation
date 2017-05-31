/**
 * Created by Arthur Deschamps on 30.05.17.
 */

import company.Product;
import jedis.JedisManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

public class companyTest {

    private Product product;
    private String id;

    @Before
    public void setUp() {
        product = new Product("Basket Ball","USA",20.0f,0.6f,false);
        id = product.getId();
    }

    @After
    public void tearDown() {}

    @Test
    public void testDatabase() {
        product.save();
        Product newProduct = (Product) JedisManager.getInstance().retrieve(product);
        Assert.assertEquals(newProduct,product);
        product.delete();
    }
}
