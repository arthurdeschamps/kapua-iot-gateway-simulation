package simulator;

import company.main.Company;
import company.product.ProductType;
import jedis.JedisManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Created by Arthur Deschamps on 05.06.17.
 * This test is supposed to verify if everything works with the economy simulation
 * TODO: consider probabilities..
 */
public class CompanySimulatorRunnerTest {

    private Company company;
    private EconomySimulatorRunner economySimulatorRunner;
    private CompanySimulatorRunner companySimulatorRunner;
    private Thread economyThread;
    private Thread companyThread;

    @Before
    public void setUp() {
        JedisManager.getInstance().flushDB();
        CompanyGenerator companyGenerator = new CompanyGenerator();
        company = companyGenerator.generateDefault();
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
                if (initialProductTypesSize != company.getProductTypes().size()) {
                    break;
                }
            }

            Assert.assertFalse(initialProductTypesSize == company.getProductTypes().size());
                
        } catch (Exception e) {
            Assert.fail();
            e.printStackTrace();
        }
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

    }

    @Test
    public void testProductTypeDestruction() {
        try {
            // If product quantity = product containing orders = 0, 1/1000 chances that the product gets destroyed
            if (company.getProductTypes().size() == 0)
                company.newProductType(DataGenerator.getInstance().generateRandomProductType());

            final ProductType productType = company.getProductTypes().get(0);
            for (int i = 0; i < Math.pow(10, 6); i++) {
                economyThread.run();
                companyThread.run();
                if (!company.getProductTypes().contains(productType))
                    break;
            }

            Assert.assertFalse(company.getProductTypes().contains(productType));

        } catch (Exception e) {
            Assert.fail();
            e.printStackTrace();
        }
    }

    @After
    public void clean() {
        JedisManager.getInstance().flushDB();
    }
}
