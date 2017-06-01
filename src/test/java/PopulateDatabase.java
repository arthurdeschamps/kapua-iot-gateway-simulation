import company.Product;
import jedis.JedisManager;
import org.junit.Test;

/**
 * Created by Arthur Deschamps on 01.06.17.
 * Generates default data for simulation
 */
public class PopulateDatabase {

    @Test
    public void generateDatabase() {
        deleteAll();
        generateProducts();
    }

    private void generateProducts() {
        Product product;

        product = new Product("Apple","Germany",0.5f,0.2f,false);
        product.save();
        product = new Product("Diamond","Brasil",1000.0f,1.4f,true);
        product.save();
        product = new Product("Basket ball","USA",30.0f,1.0f,false);
        product.save();
        product = new Product("Crystal glass","France",198.4f,1.5f,true);
        product.save();
        product = new Product("Sony QLED TV","Japan",4509.99f,20.4f,true);
        product.save();
    }

    private void deleteAll() {
        JedisManager.getInstance().getResource().flushDB();
    }
}
