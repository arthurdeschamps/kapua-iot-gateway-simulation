package simulator.runner;

import company.main.Company;
import company.product.Product;
import company.product.ProductType;
import jedis.JedisManager;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import simulator.generator.CompanyGenerator;
import simulator.main.Parametrizer;
import simulator.main.SupplyChainControlSimulator;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by Arthur Deschamps on 14.06.17.
 */
public class SimulatorTest {

    private static EconomySimulatorRunner economySimulator;
    private static CompanySimulatorRunner companySimulator;
    private static Company company;
    private static final Logger logger = Logger.getLogger(SupplyChainControlSimulator.class.getName());


    @BeforeClass
    public static void setUp() {

        company = new CompanyGenerator().generateDefaultCompany();

        economySimulator = new EconomySimulatorRunner();
        companySimulator = new CompanySimulatorRunner(company, economySimulator);
        SupplyChainControlSimulator supplyChainControlSimulator = new SupplyChainControlSimulator();
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
        List<Product> programProductStore = null;
        List<Product> dbProductStore = null;
        try {
            final int initialProductSize = company.getProducts().size();
            for (int i = 0; i < Math.pow(10, 6); i++) {
                if (initialProductSize != company.getProducts().size()) {
                    programProductStore = company.getProducts();
                    dbProductStore = JedisManager.getInstance().retrieveAllFromClass(Product.class);
                    break;
                }
            }
            Assert.assertNotNull(dbProductStore);
            Assert.assertNotNull(programProductStore);

        } catch (Exception e) {
            Assert.fail();
            e.printStackTrace();
        }

        Assert.assertEquals(programProductStore.size(), dbProductStore.size());
    }
}
