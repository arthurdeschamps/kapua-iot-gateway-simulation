package simulation.simulators.runners;

import company.company.Company;
import company.delivery.Delivery;
import company.delivery.DeliveryStatus;
import company.order.Order;
import economy.Economy;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import simulation.simulators.SupplyChainControlSimulator;
import simulation.generators.CompanyGenerator;
import simulation.generators.DataGenerator;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by Arthur Deschamps on 14.06.17.
 * @author Arthur Deschamps
 * @since 1.0
 */
public class CompanyAbstractSimulatorInterfaceTest {

    private static EconomySimulatorRunner economySimulator;
    private static CompanySimulatorRunner companySimulator;
    private static Company company;
    private static Economy economy;
    private static final Logger logger = Logger.getLogger(SupplyChainControlSimulator.class.getName());


    @BeforeClass
    public static void setUp() {

        company = CompanyGenerator.generateLocalCompany();
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
            for (int i = 0; i < Math.pow(10, 7); i++) {
                run();
                if (totalAmountOfProducts < company.getProducts().size())
                    return;
                totalAmountOfProducts = company.getProducts().size();
            }

            Assert.fail("No product ever created.");

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
                // Speed up the test by augmenting chances for type creation
                economy.setDemand(1);
                economy.setGrowth(10);
                economy.setSectorConcurrency(0);

                run();
                if (productTypesSize < company.getProductTypes().size())
                    return;
                productTypesSize = company.getProductTypes().size();
            }

            Assert.fail("No product type ever created.");

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
                // Removes every orders (higher chance that a type is going to be destructed)
                company.getOrders().removeAll(company.getOrders());
                if (currentSize > company.getProductTypes().size())
                    return;
                currentSize = company.getProductTypes().size();
            }

            Assert.fail("No product type ever destroyed.");

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

            Assert.fail("No order ever made by any customer.");
        } catch (Exception e) {
            Assert.fail();
            e.printStackTrace();
        }
    }

    @Test
    public void testDeliveryStatusUpdate() {
        try {
            DataGenerator dataGenerator = new DataGenerator(company);
            final Optional<Order> orderOptional = dataGenerator.generateRandomOrder();
            Assert.assertTrue(orderOptional.isPresent());
            company.newOrder(orderOptional.get());
            final Delivery delivery = dataGenerator.generateRandomDelivery().get();
            Assert.assertEquals(DeliveryStatus.WAREHOUSE,delivery.getDeliveryState());
            company.newDelivery(delivery);
            Assert.assertTrue(company.getDeliveries().contains(delivery));

            for (int i = 0; i < Math.pow(10, 7); i++) {
                run();
                if (delivery.getDeliveryState().equals(DeliveryStatus.TRANSIT))
                    return;
            }

            Assert.fail("Delivery status never passed from 'warehouse' to 'shipped'.");
        } catch (Exception e) {
            Assert.fail();
            e.printStackTrace();
        }
    }

}
