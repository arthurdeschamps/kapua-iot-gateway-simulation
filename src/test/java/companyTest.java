/**
 * Created by Arthur Deschamps on 30.05.17.
 */

import company.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class companyTest {

    private Product product;

    @Before
    public void setUp() {
        product = new Product("Basket Ball","USA",20.0f,0.6f,false);
    }

    @After
    public void tearDown() {}

    @Test
    public void testDatabase() {
        product.save();
    }
}
