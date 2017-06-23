package simulator.runner;

import company.delivery.Delivery;
import company.main.Company;
import company.order.Order;
import company.product.Product;
import company.product.ProductType;
import company.transportation.Transportation;
import economy.Economy;
import simulator.generator.DataGenerator;
import simulator.util.ProbabilityUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * This runnable simulates a company's production and delivery chain based on the given economy simulator metrics.
 * The base unit of this runnable is 1 second. This means that 1 execution of the method run is equivalent to 1 second
 * elapsed in real time.
 *
 * @author Arthur Deschamps
 * @since 1.0
 * @see EconomySimulatorRunner
 */
public class CompanySimulatorRunner implements Runnable {

    private DataGenerator dataGenerator;
    private final ProbabilityUtils probability = new ProbabilityUtils();

    private Company company;
    private Economy economy;

    public CompanySimulatorRunner(Company company, Economy economy) {
        this.company = company;
        this.economy = economy;
        this.dataGenerator = new DataGenerator(this.company);
    }

    /**
     * Calls every simulation method.
     * @since 1.0
     */
    @Override
    public void run() {
        try {
            simulateProducts();
            simulateCustomersBehavior();
            simulateOrders();
            simulateDeliveries();
            simulateTransportation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls every simulation method related to products (products, product types, etc)
     */
    private void simulateProducts() {
        simulateProductTypeCreation();
        simulateProduction();
        simulatePriceCuts();
        simulateProductTypeDestruction();
    }

    /**
     * Simulates creation of products. Production is inversely proportional to the current stock of the product's type.
     * @since 1.0
     */
    private void simulateProduction () {
        // A product with low quantity has high chances to get its stock refilled
        int productQuantity;
        for (final ProductType productType : company.getProductTypes()) {

            // Cast is safe since even if we have billions of products, it won't be refilled anyway
            productQuantity = (int) company.getProductQuantity(productType);

            //Check the product stock on average twice a day
            if (probability.event(2, ProbabilityUtils.TimeUnit.DAY)) {
                // Stock must be 3 times the number of orders (for a particular product type)
                if (productQuantity <= company.getOrdersFromProductType(productType).size()*3) {
                    for (int i = 0; i <= new Random().nextInt(100)+10; i++)
                        company.newProduct(new Product(productType,company.getHeadquarters().getCoordinates()));
                }
            }
        }
    }

    /**
     * Simulates creation of a type of product of the company.
     * @since 1.0
     */
    private void simulateProductTypeCreation() {
        // A new product type is likely to be created when the economy is well, the demand is weak or the concurrency is high
        if (probability.event(Math.abs(economy.getGrowth()-economy.getDemand()+economy.getSectorConcurrency()), ProbabilityUtils.TimeUnit.WEEK)) {
            final ProductType productType = DataGenerator.generateRandomProductType();
            company.newProductType(productType);
        }
    }

    /**
     * Simulates destruction o a type of product of the company.
     * @since 1.0
     */
    private void simulateProductTypeDestruction() {
        for (final ProductType productType : company.getProductTypes()) {
            // Sometimes when no order on the product's type exist, company decides to get rid of it
            // Company must have at least 2 types of products
            if (company.getOrdersFromProductType(productType).size() == 0 && company.getProductTypes().size() > 2) {
                if (probability.event(2, ProbabilityUtils.TimeUnit.WEEK)) {
                    company.deleteProductType(productType);
                    return;
                }
            }
        }
    }

    /**
     * Simulates discounts on products.
     * @since 1.0
     */
    private void simulatePriceCuts() {
        for(final ProductType productType : company.getProductTypes()) {
            if (probability.event(company.getOrdersFromProductType(productType).size()/100, ProbabilityUtils.TimeUnit.MONTH)) {
                company.getProducts().forEach(product -> {
                    // 10 % discount
                    final float cutPrice = product.getPrice()*90/100;
                    // Limit price cut to half the original price
                    final float basePrice = product.getProductType().getBasePrice();
                    if (cutPrice >= basePrice/2)
                        product.setPrice(cutPrice);
                });
            }
        }
    }

    /**
     * Simulates new orders.
     * @since 1.0
     */
    private void simulateOrders() throws Exception {
        // No customer means no order
        if (company.getCustomerStore().getRandom().isPresent() && company.getProducts().size() > 0) {
            // A product has probability nbrCustomers/(price*10^4) to be ordered
            List<Product> orderedProducts = new ArrayList<>();
            for (final Product product : company.getProducts()) {
                if (probability.event(company.getCustomers().size(), ProbabilityUtils.TimeUnit.HOUR))
                    orderedProducts.add(product);
            }
            // Make sure the order is not empty
            if (orderedProducts.size() > 0)
                company.newOrder(new Order(company.getCustomerStore().getRandom().get(), orderedProducts));
        }
    }

    /**
     * Simulates new deliveries.
     * @since 1.0
     */
    private void simulateDeliveries() {
        Iterator<Order> iterator = company.getOrders().iterator();
        while (iterator.hasNext()) {
            if (probability.event(1, ProbabilityUtils.TimeUnit.HOUR)) {
                final Order order = iterator.next();
                company.getAvailableTransportation().ifPresent(transportation -> company.getDeliveryStore().add(
                        new Delivery(order,transportation,company.getHeadquarters(),order.getBuyer().getAddress())
                ));
                iterator.remove();
            }
        }
    }

    /**
     * Calls transportation acquisition as well as destruction/abandon simulation methods.
     * @since 1.0
     */
    private void simulateTransportation() {
        simulateTransportationAcquisitions();
        simulateTransportationDestruction();
    }

    /**
     * Simulates acquisition of new customers of lost of customers for the company.
     * @since 1.0
     */
    private void simulateCustomersBehavior() {
        if (economy.getGrowth() > 0) {
            // If economy growth is high, there is a high chance of getting a new customer
            if (probability.event(Math.abs(economy.getGrowth()*2), ProbabilityUtils.TimeUnit.DAY))
                company.newCustomer(dataGenerator.generateRandomCustomer());
            // It can still lose customers sometimes
            if (probability.event(Math.abs(economy.getGrowth()), ProbabilityUtils.TimeUnit.WEEK))
                company.deleteRandomCustomer();
        } else {
            // If economy growth is negative, there is a chance that our company might lose customers
            if (probability.event(Math.abs(economy.getGrowth()), ProbabilityUtils.TimeUnit.DAY))
                company.deleteRandomCustomer();
            // Company can still acquire a new customer
            if (probability.event(Math.abs(economy.getGrowth()), ProbabilityUtils.TimeUnit.WEEK))
                company.newCustomer(dataGenerator.generateRandomCustomer());
        }
    }

    /**
     * Simulates transportation acquisition
     * @since 1.0
     */
    private void simulateTransportationAcquisitions() {
        // If orders >= nbr transportation * 100, new transportation should be acquired
        if (company.getOrders().size() >= company.getAllTransportation().size()*100) {
            // On average takes 2 weeks to be done
            if (probability.event(0.5d, ProbabilityUtils.TimeUnit.WEEK)) {
                Transportation transportation = DataGenerator.generateRandomTransportation();
                company.newTransportation(transportation);
            }
        }
    }

    /**
     * Simulates transportation destruction
     * @since 1.0
     */
    private void simulateTransportationDestruction() {
        // If number of transportation surpasses number of orders, there is a surplus of transportation
        if (company.getOrders().size() <= company.getAllTransportation().size()) {
            // Takes on average two weeks to get rid of
            if (probability.event(2, ProbabilityUtils.TimeUnit.WEEK)) {
                company.getAvailableTransportation().ifPresent(transportation ->
                        company.deleteTransportation(transportation));
            }
        }
    }


    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
