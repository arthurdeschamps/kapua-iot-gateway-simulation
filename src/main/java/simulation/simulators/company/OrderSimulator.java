package simulation.simulators.company;

import company.company.Company;
import company.customer.Customer;
import company.order.Order;
import company.product.Product;
import economy.Economy;
import simulation.util.ProbabilityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Simulates everything related to company orders.
 * @since 1.0
 * @author Arthur Deschamps
 */
public class OrderSimulator extends AbstractCompanyComponentSimulator {

    public OrderSimulator(Company company, Economy economy) {
        super(company, economy);
    }

    @Override
    public void run() {
        simulateOrders();
    }

    /**
     * Simulates new orders.
     * @since 1.0
     */
    private void simulateOrders() {
        Random random = new Random();

        // No customer means no order
        for (final Customer customer : company.getCustomers()) {
            // A customer buys a product on average once a month
            if (probabilityUtils.event(1, ProbabilityUtils.TimeUnit.MONTH)) {
                final int nbrOrderedProducts = random.nextInt(10)+1;
                // A customer buys up to 10 products per order
                List<Product> orderedProducts = new ArrayList<>(nbrOrderedProducts);
                for (int i = 0; i <nbrOrderedProducts ; i++)
                    company.getProductStore().getRandom().ifPresent(orderedProducts::add);
                // Make sure the order is not empty
                if (orderedProducts.size() > 0)
                    company.newOrder(new Order(customer, orderedProducts));
            }
        }
    }
}
