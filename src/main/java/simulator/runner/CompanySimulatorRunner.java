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
 *
 * @author Arthur Deschamps
 * @since 1.0
 * @see EconomySimulatorRunner
 */
public class CompanySimulatorRunner implements Runnable {

    private static final Logger logger = Logger.getLogger(CompanySimulatorRunner.class.getName());

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
        if (event(Math.abs(economy.getGrowth()+economy.getDemand()+economy.getSectorConcurrency()),TimeUnit.YEAR)) {
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
                if (event(2,TimeUnit.MONTH)) {
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
            if (event(company.getOrdersFromProductType(productType).size()/100,TimeUnit.YEAR)) {
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
                if (event(company.getCustomers().size()/product.getPrice(),TimeUnit.MONTH))
                    orderedProducts.add(product);
            }
            // Make sure the order is not empty
            if (orderedProducts.size() > 0)
                company.newOrder(new Order(company.getCustomers().get(random.nextInt(company.getCustomers().size())), orderedProducts));
        }
    }

    /**
     * Simulates new deliveries.
     * @since 1.0
     */
    private void simulateDeliveries() {
        // An order starts to be delivered in a day on average
        for (final Order order : company.getOrders()) {
            if (event(1,TimeUnit.DAY)) {
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
            if (event(economy.getGrowth()*2,TimeUnit.MONTH))
                company.newCustomer(DataGenerator.getInstance().generateRandomCustomer());
            // It can still lose customers sometimes
            if (event(economy.getGrowth(),TimeUnit.YEAR))
                company.deleteRandomCustomer();
        } else {
            // If economy growth is negative, there is a chance that our company might lose customers
            if (event(Math.abs(economy.getGrowth()),TimeUnit.MONTH))
                company.deleteRandomCustomer();
            // Company can still acquire a new customer
            if (event(Math.abs(economy.getGrowth()),TimeUnit.YEAR))
                company.newCustomer(DataGenerator.getInstance().generateRandomCustomer());
        }
    }

    /**
     * Simulates transportation acquisition
     * @since 1.0
     */
    private void simulateTransportationAcquisitions() {
        // If orders >= nbr transportation * 100, new transportation acquired
        if (company.getOrders().size() >= company.getAllTransportation().size()*100) {
            // Not instantaneous
            if (random.nextInt(2) == 0) {
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
        // TODO
    }

    /**
     * This method returns true if an event that happens with a certain frequency (in a certain time unit) happened
     * and false if it did not happen. The only computation done is a Random.nextInt(nbr) with nbr depending on the
     * frequency and time unit.
     * @param frequency
     * Frequency at which the event occurs.
     * @param timeUnit
     * The frequency's unit. See TimeUnit enumeration of this class.
     * @return
     * A boolean value indicating if the event occured (true) or not (false).
     *
     * @since 1.0
     */
    private boolean event(double frequency, TimeUnit timeUnit) {
        // Convert frequency from initial time unit to months in order to have a common unit
        double normalizedFrequency = TimeUnit.convertToMonth(frequency,timeUnit);
        // Convert frequency into number of outcomes
        while (normalizedFrequency % 1 != 0) {
            normalizedFrequency *= 10;
        }
        // Convert into integer
        int nbrOutcomes = (int) normalizedFrequency;
        // Simulate event
        Random random = new Random();
        return random.nextInt(nbrOutcomes) == 0;
    }

    /**
     * TimeUnit allows simulation functions to indicate the time unit of the frequency at each the simulated event
     * occurs on average.
     * @since 1.0
     */
    private enum TimeUnit {
        HOUR, DAY, WEEK, MONTH, YEAR;

        public static double convertToMonth(double value, TimeUnit timeUnit) {
            switch (timeUnit) {
                case YEAR:
                    return value*12;
                case MONTH:
                    return value;
                case WEEK:
                    return value/4;
                case DAY:
                    return value/(4*7);
                case HOUR:
                    return value/(4*7*24);
            }
            throw new EnumConstantNotPresentException(TimeUnit.class,"Given TimeUnit not recognized");
        }
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
