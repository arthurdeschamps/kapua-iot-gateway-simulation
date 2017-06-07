package simulator;

import company.customer.Customer;
import company.main.Company;

import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by Arthur Deschamps on 06.06.17.
 */
public class CompanySimulatorRunner implements Runnable {

    private Company company;
    private EconomySimulatorRunner economy;
    private Random random;

    public CompanySimulatorRunner(Company company, EconomySimulatorRunner economy) {
        this.company = company;
        this.economy = economy;
        random = new Random();
    }

    @Override
    public void run() {
        try {
            simulateCustomersBehavior();
            simulateNewOrders();
            simulateDeliveries();
            simulateProductsMovement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void simulateNewOrders() {}

    private void simulateDeliveries() {}

    private void simulateProductsMovement() {}

    private void simulateCustomersBehavior() {
        final Logger logger = Logger.getLogger(CompanySimulatorRunner.class.getName());
        if (economy.getGrowth() > 0) {
            // If economy growth is high, there is a high chance of getting a new customer
            if (random.nextInt(100000) <= economy.getGrowth()) {
                company.getCustomerStore().add(DefaultDataGenerator.generateRandomCustomer());
                logger.info("New customer");
            }
        } else {
            // If economy growth is negative, there is a chance that our company might lose customers
            if (random.nextInt(100000) <= Math.abs(economy.getGrowth())) {
                company.getCustomerStore().removeRandomCustomer();
                logger.info("Customer lost");
            }
        }
    }
}
