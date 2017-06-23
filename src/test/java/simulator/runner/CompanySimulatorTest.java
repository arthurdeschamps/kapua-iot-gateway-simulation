package simulator.runner;

import company.main.Company;
import economy.Economy;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import simulator.generator.CompanyGenerator;
import simulator.generator.DataGenerator;
import simulator.main.SupplyChainControlSimulator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by Arthur Deschamps on 14.06.17.
 * @author Arthur Deschamps
 * @since 1.0
 */
public class CompanySimulatorTest {

    private static EconomySimulatorRunner economySimulator;
    private static CompanySimulatorRunner companySimulator;
    private static Company company;
    private static Economy economy;
    private static final Logger logger = Logger.getLogger(SupplyChainControlSimulator.class.getName());


    @BeforeClass
    public static void setUp() {

        company = new CompanyGenerator().generateDefaultCompany();
        economy = new Economy();
        // Make sure company has client to speed up the tests
        for (int i = 0; i < 10; i++) {
            company.newCustomer(new DataGenerator(company).generateRandomCustomer());
        }

        economySimulator = new EconomySimulatorRunner(economy);
        companySimulator = new CompanySimulatorRunner(company, economy);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        // Display data
        executor.scheduleWithFixedDelay(() -> logger.info("Growth: "+economy.getGrowth()+", Demand: "+economy.getDemand()
               +", Sector concurrency: "+economy.getSectorConcurrency()),1,30,TimeUnit.SECONDS);
        executor.scheduleWithFixedDelay(() -> logger.info("Products: "+Integer.toString(company.getProducts().size())+", Types: "+
                Integer.toString(company.getProductTypes().size())+
        ", Orders: "+Integer.toString(company.getOrders().size())+", Deliveries: "+Integer.toString(company.getDeliveries().size())
                +", Transportation: "+Integer.toString(company.getAllTransportation().size())+
        ", Customers:"+Integer.toString(company.getCustomers().size())),0,5,TimeUnit.SECONDS);
    }

    private void run() {
        economySimulator.run();
        companySimulator.run();
    }

    @Test
    public void testProductProduction() {
        /*
            Now that we have a product or multiple product types, we should be able to create products
         */
        try {
            int totalAmountOfProducts = company.getProducts().size();
            for (int i = 0; i < Math.pow(10, 10); i++) {
                run();
                if (totalAmountOfProducts < company.getProducts().size())
                    return;
                totalAmountOfProducts = company.getProducts().size();
            }

            Assert.fail();

        } catch (Exception e) {
            Assert.fail();
            e.printStackTrace();
        }

    }

    @Test
    public void testProductTypeCreation() {
        try {
            int productTypesSize = company.getProductTypes().size();
            for (int i = 0; i < Math.pow(10, 6); i++) {
                run();
                if (productTypesSize < company.getProductTypes().size())
                    return;
                productTypesSize = company.getProductTypes().size();
            }

            Assert.fail();

        } catch (Exception e) {
            Assert.fail();
            e.printStackTrace();
        }
    }

    @Test
    public void testProductTypeDestruction() {
        try {
            int currentSize = company.getProductTypes().size();

            for (int i = 0; i < Math.pow(10, 7); i++) {
                run();
                if (currentSize > company.getProductTypes().size())
                    return;
                currentSize = company.getProductTypes().size();
            }

            Assert.fail();

        } catch (Exception e) {
            Assert.fail();
            e.printStackTrace();
        }
    }

    @Test
    public void testOrders() {
        try {
            int orderAmount = company.getOrders().size();
            for (int i = 0; i < Math.pow(10, 7); i++) {
                run();
                if (orderAmount < company.getOrders().size())
                    return;
                orderAmount = company.getOrders().size();
            }

            Assert.fail();
        } catch (Exception e) {
            Assert.fail();
            e.printStackTrace();
        }
    }

    @Test
    public void testProductMovement() {
        try {
            int numberOfDeliveries = company.getDeliveries().size();
            for (int i = 0; i < Math.pow(10, 8); i++) {
                run();
                if (numberOfDeliveries > company.getDeliveries().size())
                    return;
                numberOfDeliveries = company.getDeliveries().size();
            }

            Assert.fail();
        } catch (Exception e) {
            Assert.fail();
            e.printStackTrace();
        }
    }

}
