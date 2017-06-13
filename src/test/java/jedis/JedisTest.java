package jedis;

import company.main.Company;
import company.product.ProductType;
import company.transportation.Transportation;
import org.junit.Assert;
import org.junit.Test;
import simulator.generator.CompanyGenerator;
import simulator.generator.DataGenerator;

/**
 * Created by Arthur Deschamps on 11.06.17.
 */
public class JedisTest {

    @Test
    public void testAddObject() {
        Company company = new CompanyGenerator().generateDefaultCompany();
        final int initialSize = company.getCustomers().size();
        company.newCustomer(DataGenerator.getInstance().generateRandomCustomer());
        Assert.assertEquals(initialSize+1,company.getCustomers().size());
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
}
