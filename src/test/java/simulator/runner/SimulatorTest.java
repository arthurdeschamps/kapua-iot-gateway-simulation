package simulator.runner;

import company.main.Company;
import company.product.Product;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import simulator.generator.CompanyGenerator;
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
public class SimulatorTest {

    private static EconomySimulatorRunner economySimulator;
    private static CompanySimulatorRunner companySimulator;
    private static Company company;
    private static final Logger logger = Logger.getLogger(SupplyChainControlSimulator.class.getName());


    @BeforeClass
    public static void setUp() {

        company = CompanyGenerator.getInstance().generateDefaultCompany();

        economySimulator = new EconomySimulatorRunner();
        companySimulator = new CompanySimulatorRunner(company, economySimulator);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        executor.scheduleWithFixedDelay(economySimulator,0,1,TimeUnit.MICROSECONDS);
        executor.scheduleWithFixedDelay(companySimulator,0,1,TimeUnit.MICROSECONDS);
        // Display data
        executor.scheduleWithFixedDelay(() -> logger.info("Growth: "+economySimulator.getGrowth()+", Demand: "+economySimulator.getDemand()
                +", Sector concurrency: "+economySimulator.getSectorConcurrency()),1,5,TimeUnit.SECONDS);
    }

    @Test
    public void testProductProduction() {
        /*
            Now that we have a product or multiple product types, we should be able to create products
         */
        boolean newProduct = false;
        try {
            final int initialProductSize = company.getProducts().size();
            for (int i = 0; i < Math.pow(10, 6); i++) {
                if (initialProductSize != company.getProducts().size()) {
                    newProduct = true;
                    break;
                }
            }

            Assert.assertTrue(newProduct);

        } catch (Exception e) {
            Assert.fail();
            e.printStackTrace();
        }

    }
    
}
