package simulation.simulators.runners;

import company.company.Company;
import economy.Economy;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simulation.generators.CompanyGenerator;
import simulation.generators.DataGenerator;
import simulation.simulators.SupplyChainControlSimulator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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
    private static final Logger logger = LoggerFactory.getLogger(SupplyChainControlSimulator.class);


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

}
