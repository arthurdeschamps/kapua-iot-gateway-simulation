package jedis;

import company.delivery.Delivery;
import company.main.Company;
import company.product.ProductType;
import company.product.ProductTypeStore;
import company.transportation.Transportation;
import org.junit.Assert;
import org.junit.Test;
import simulator.generator.CompanyGenerator;
import simulator.generator.DataGenerator;

/**
 * Created by Arthur Deschamps on 11.06.17.
 * Test of JedisManager class
 * @since 1.0
 * @see JedisManager
 */
public class JedisTest {

    @Test
    public void testAddObject() {
        Company company = new CompanyGenerator().generateDefaultCompany();
        final int initialSize = company.getCustomers().size();
        for (int i = 0; i < 100; i++) {
            company.newCustomer(DataGenerator.getInstance().generateRandomCustomer());
        }
        Assert.assertEquals(initialSize+100,company.getCustomers().size());
    }

    @Test
    public void testFlushAll() {
        JedisManager.getInstance().flushAll();
        Assert.assertEquals(JedisManager.getInstance().retrieveAllFromClass(ProductType.class).size(),0);
        Assert.assertEquals(JedisManager.getInstance().retrieveAllFromClass(Delivery.class).size(),0);
    }

    @Test
    public void testRetrieveAllFromClass() {
        Company company = new CompanyGenerator().generateDefaultCompany();
        int initialSize = JedisManager.getInstance().retrieveAllFromClass(Transportation.class).size();
        company.newTransportation(DataGenerator.getInstance().generateRandomTransportation());
        company.newTransportation(DataGenerator.getInstance().generateRandomTransportation());
        Assert.assertEquals(initialSize+2,JedisManager.getInstance().retrieveAllFromClass(Transportation.class).size());
        initialSize = JedisManager.getInstance().retrieveAllFromClass(ProductType.class).size();
        company.newProductType(DataGenerator.getInstance().generateRandomProductType());
        company.newProductType(DataGenerator.getInstance().generateRandomProductType());
        company.newProductType(DataGenerator.getInstance().generateRandomProductType());
        Assert.assertEquals(initialSize+3,JedisManager.getInstance().retrieveAllFromClass(ProductType.class).size());
    }

    @Test
    public void testAddProductTypes() {
        JedisManager.getInstance().flushAll();
        ProductTypeStore productTypeStore = new ProductTypeStore();
        for (int i = 0; i < 300; i++)
            productTypeStore.add(DataGenerator.getInstance().generateRandomProductType());
        Assert.assertEquals(productTypeStore.getStorage().size(),300);
        Assert.assertEquals(productTypeStore.getStorage().size(),JedisManager.getInstance().retrieveAllFromClass(ProductType.class).size());
    }
}
