package simulator.runner;

import company.delivery.Delivery;
import company.main.Company;
import company.order.Order;
import company.product.Product;
import company.product.ProductType;
import jedis.JedisManager;
import org.junit.*;
import simulator.generator.CompanyGenerator;

import java.util.logging.Logger;


/**
 * Created by Arthur Deschamps on 05.06.17.
 * This class test is supposed to verify if everything works with the economy simulation
 *
 * @author Arthur Deschamps
 * @since 1.0
 * @see CompanySimulatorRunner
 */
public class CompanySimulatorRunnerTest {

    private static Company company;
    private static Thread economyThread;
    private static Thread companyThread;

    @BeforeClass
    public static void setUp() {

        EconomySimulatorRunner economySimulatorRunner;
        CompanySimulatorRunner companySimulatorRunner;
        company = new CompanyGenerator().generateDefaultCompany();
        economySimulatorRunner = new EconomySimulatorRunner();
        companySimulatorRunner = new CompanySimulatorRunner(company, economySimulatorRunner);
        economyThread = new Thread(economySimulatorRunner);
        companyThread  = new Thread(companySimulatorRunner);
    }

    @Test
    public void testProductTypeCreation() {
        /*
         * Let growth ~ 0
         * Let demand ~ 1/2
         * Let concurrency ~ 1/2
         * ProductType should be created in 10^5 seconds on average
         * If it's not after 1 microseconds, there is a problem
         */
        try {
            final int initialProductTypesSize = company.getProductTypes().size();
            for (int i = 0; i < Math.pow(10,6); i++) {
                economyThread.run();
                companyThread.run();
            }

            Assert.assertFalse(initialProductTypesSize == company.getProductTypes().size());
                
        } catch (Exception e) {
            Assert.fail();
            e.printStackTrace();
        }
        Assert.assertEquals(company.getProductTypes().size(), JedisManager.getInstance().retrieveAllFromClass(ProductType.class).size());
    }

    @Test
    public void testProductProduction() {
        /*
            Now that we have a product or multiple product types, we should be able to create products
         */
        try {
            final int initialProductSize = company.getProducts().size();
            for (int i = 0; i < Math.pow(10, 6); i++) {
                economyThread.run();
                companyThread.run();
                if (initialProductSize != company.getProducts().size()) {
                    break;
                }
            }

            Assert.assertFalse(initialProductSize == company.getProducts().size());

        } catch (Exception e) {
            Assert.fail();
            e.printStackTrace();
        }

        Assert.assertEquals(company.getProducts().size(), JedisManager.getInstance().retrieveAllFromClass(Product.class).size());
    }

    @Test
    public void testProductTypeDestruction() {
        try {
            // If product quantity = product containing orders = 0, 1/1000 chances that the product gets destroyed
            int productTypeSize = company.getProductTypes().size();
            for (int i = 0; i < Math.pow(10, 6); i++) {
                economyThread.run();
                companyThread.run();
                if (productTypeSize > company.getProductTypes().size()) {
                    break;
                } else {
                    productTypeSize = company.getProductTypes().size();
                }
            }

            Assert.assertFalse(productTypeSize == company.getProductTypes().size());

        } catch (Exception e) {
            Assert.fail();
            e.printStackTrace();
        }

        Assert.assertEquals(company.getProductTypes().size(),JedisManager.getInstance().retrieveAllFromClass(ProductType.class).size());
    }

    @Test
    public void testOrders() {
        try {
            int orderSize = company.getOrders().size();
            for (int i = 0; i < Math.pow(10,6); i++) {
                economyThread.run();
                companyThread.run();
                if (orderSize < company.getOrders().size()) {
                    break;
                } else {
                    orderSize = company.getOrders().size();
                }
            }

            Assert.assertFalse(orderSize == company.getOrders().size());
        } catch (Exception e) {
            Assert.fail();
            e.printStackTrace();
        }

        Assert.assertEquals(company.getOrders().size(),JedisManager.getInstance().retrieveAllFromClass(Order.class).size());
    }

    @Test
    public void testDeliveries() {
        try {
            int deliverySize = company.getDeliveries().size();
            for (int i = 0; i < Math.pow(10,6);i++) {
                economyThread.run();
                companyThread.run();
                if (deliverySize < company.getDeliveries().size()) {
                    break;
                } else {
                    deliverySize = company.getDeliveries().size();
                }
            }
            Assert.assertFalse(deliverySize == company.getDeliveries().size());
        } catch (Exception e) {
            Assert.fail();
            e.printStackTrace();
        }

        Assert.assertEquals(company.getDeliveries().size(),JedisManager.getInstance().retrieveAllFromClass(Delivery.class).size());
    }

    @AfterClass
    public static void clean() {
        JedisManager.getInstance().flushDB();
    }

}
