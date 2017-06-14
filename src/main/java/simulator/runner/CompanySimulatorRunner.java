package simulator.runner;

import company.delivery.Delivery;
import company.main.Company;
import company.order.Order;
import company.product.Product;
import company.product.ProductType;
import company.transportation.Transportation;
import simulator.generator.DataGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Logger;

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

    private static final Logger logger = Logger.getLogger(CompanySimulatorRunner.class.getName());
    private final ProbabilitySimulator probability = new ProbabilitySimulator();

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
     * Calls every simulation method.
     * @since 1.0
     */
    private void simulateProducts() {
        simulateProductTypeCreation();
        simulateProduction();
        simulateProductsMovement();
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

            // + 10 to not automatically refill every second when the stock is down
            if (random.nextInt(productQuantity+100) == 0) {
                company.newProduct(new Product(productType,company.getHeadquarters().toCoordinates(),productType.getBasePrice()));
                //logger.info("New product of type "+productType.getName()+" produced");
            }

        }
    }

    /**
     * Simulates creation of a type of product of the company.
     * @since 1.0
     */
    private void simulateProductTypeCreation() {
        // A new product type is likely to be created when the economy is well, the demand is weak or the concurrency is high
        if (probability.event(Math.abs(economy.getGrowth()+economy.getDemand()+economy.getSectorConcurrency()), ProbabilitySimulator.TimeUnit.YEAR)) {
            final ProductType productType = DataGenerator.getInstance().generateRandomProductType();
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
                if (probability.event(2, ProbabilitySimulator.TimeUnit.MONTH)) {
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
            if (probability.event(company.getOrdersFromProductType(productType).size()/100, ProbabilitySimulator.TimeUnit.YEAR)) {
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
    private void simulateOrders() {
        // No customer means no order
        if (company.getCustomers().size() > 0 && company.getProducts().size() > 0) {
            // A product has probability nbrCustomers/(price*10^4) to be ordered
            List<Product> orderedProducts = new ArrayList<>();
            for (final Product product : company.getProducts()) {
                if (probability.event(company.getCustomers().size()/product.getPrice(),ProbabilitySimulator.TimeUnit.MONTH))
                    orderedProducts.add(product);
            }
            // Make sure the order is not empty
            if (orderedProducts.size() > 0)
                company.newOrder(new Order(company.getCustomerStore().getRandom(), orderedProducts));
        }
    }

    /**
     * Simulates new deliveries.
     * @since 1.0
     */
    private void simulateDeliveries() {
        // An order starts to be delivered in a day on average
        for (final Order order : company.getOrders()) {
            if (probability.event(1,ProbabilitySimulator.TimeUnit.DAY)) {
                Optional<Transportation> potentialTransportation = company.getAvailableTransportation();
                if (potentialTransportation != null && potentialTransportation.isPresent()) {
                    company.newDelivery(new Delivery(order, company.getAvailableTransportation().get(), company.getHeadquarters(),
                            company.getHeadquarters().toCoordinates(), order.getBuyer().getPostalAddress()));
                }
            }
        }
    }

    /**
     * Simulates products movement during deliveries.
     * @since 1.0
     */
    private void simulateProductsMovement() {

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
            if (probability.event(economy.getGrowth()*2,ProbabilitySimulator.TimeUnit.MONTH))
                company.newCustomer(DataGenerator.getInstance().generateRandomCustomer());
            // It can still lose customers sometimes
            if (probability.event(economy.getGrowth(),ProbabilitySimulator.TimeUnit.YEAR))
                company.deleteRandomCustomer();
        } else {
            // If economy growth is negative, there is a chance that our company might lose customers
            if (probability.event(Math.abs(economy.getGrowth()),ProbabilitySimulator.TimeUnit.MONTH))
                company.deleteRandomCustomer();
            // Company can still acquire a new customer
            if (probability.event(Math.abs(economy.getGrowth()),ProbabilitySimulator.TimeUnit.YEAR))
                company.newCustomer(DataGenerator.getInstance().generateRandomCustomer());
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
            if (probability.event(0.5d,ProbabilitySimulator.TimeUnit.WEEK)) {
                Transportation transportation = DataGenerator.getInstance().generateRandomTransportation();
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
            if (probability.event(2,ProbabilitySimulator.TimeUnit.MONTH)) {
                Optional<Transportation> transportationToDelete = company.getAvailableTransportation();
                transportationToDelete.ifPresent(transportation -> company.deleteTransportation(transportation));
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
