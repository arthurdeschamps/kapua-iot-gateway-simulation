import company.Product;
import company.ProductStore;
import jedis.JedisManager;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Arthur Deschamps on 01.06.17.
 * Generates default data for simulation
 */
public class PopulateDatabase {

    ProductStore productStore;

    @Before
    public void setUp() {
        productStore = new ProductStore();
    }

    @Test
    public void generateDatabase() {
        deleteAll();
        generateProducts();
    }

    private void generateProducts() {
        productStore.add(new Product("Apple","Germany",0.5f,0.2f,false));
        productStore.add(new Product("Diamond","Brasil",1000.0f,1.4f,true));
        productStore.add(new Product("Basket ball","USA",30.0f,1.0f,false));
        productStore.add(new Product("Crystal glass","France",198.4f,1.5f,true));
        productStore.add(new Product("Sony QLED TV","Japan",4509.99f,20.4f,true));
    }

    private void deleteAll() {
        JedisManager.getInstance().getResource().flushDB();
    }
}
