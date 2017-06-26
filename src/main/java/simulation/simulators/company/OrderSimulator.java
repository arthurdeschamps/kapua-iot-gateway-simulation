package simulation.simulators.company;

import company.company.Company;
import company.order.Order;
import company.product.Product;
import economy.Economy;
import simulation.util.ProbabilityUtils;

import java.util.ArrayList;
import java.util.List;

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
        // No customer means no order
        if (company.getCustomerStore().getRandom().isPresent() && company.getProducts().size() > 0) {
            // A product has probability nbrCustomers/(price*10^4) to be ordered
            List<Product> orderedProducts = new ArrayList<>();
            for (final Product product : company.getProducts()) {
                if (probabilityUtils.event(company.getCustomers().size(), ProbabilityUtils.TimeUnit.HOUR))
                    orderedProducts.add(product);
            }
            // Make sure the order is not empty
            if (orderedProducts.size() > 0)
                company.newOrder(new Order(company.getCustomerStore().getRandom().get(), orderedProducts));
        }
    }
}
