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
 * Created by Arthur Deschamps on 06.06.17.
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

    private void simulateProducts() {
        simulateProductTypeCreation();
        simulateProduction();
        simulateProductsMovement();
        simulatePriceCuts();
        simulateProductTypeDestruction();
    }

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

    private void simulateProductTypeCreation() {
        // A new product type is likely to be created when the economy is well, the demand is weak or the concurrency is high
        int nbrOutcomes = (int) (Math.pow(10,5) - 100*(economy.getGrowth() - economy.getDemand() + economy.getSectorConcurrency()));
        if (random.nextInt(nbrOutcomes) == 0) {
            final ProductType productType = DataGenerator.getInstance().generateRandomProductType();
            company.newProductType(productType);
        }
    }

    private void simulateProductTypeDestruction() {
        for (final ProductType productType : company.getProductTypes()) {
            // Sometimes when no order on the product's type exist, company decides to get rid of it
            // Company must have at least 2 types of products
            if (company.getOrdersForProductType(productType).size() == 0 && company.getProductTypes().size() > 2) {
                if (random.nextInt(1000) == 0) {
                    company.deleteProductType(productType);
                    return;
                }
            }
        }
    }

    private void simulatePriceCuts() {
        // TODO
    }

    private void simulateOrders() {
        // No customer means no order
        if (company.getCustomers().size() > 0 && company.getProducts().size() > 0) {
            // A product has probability nbrCustomers/(price*10^4) to be ordered
            int nbrOutcomes;
            List<Product> orderedProducts = new ArrayList<>();
            for (final Product product : company.getProducts()) {
                nbrOutcomes = (int) (product.getPrice() * Math.pow(10, 4)) / company.getCustomers().size();
                if (nbrOutcomes < 1)
                    nbrOutcomes = 1;

                if (random.nextInt(nbrOutcomes) == 0) {
                    orderedProducts.add(product);
                }
            }
            if (orderedProducts.size() > 0) {
                company.newOrder(new Order(company.getCustomers().get(random.nextInt(company.getCustomers().size())), orderedProducts));
            }
        }
    }

    private void simulateDeliveries() {
        // An order starts to be delivered in a day on average
        // 1 day = 24 * 60^2 seconds
        for (final Order order : company.getOrders()) {
            if (random.nextInt(24*60*60) == 0) {
                Optional<Transportation> potentialTransportation = company.getAvailableTransportation();
                if (potentialTransportation != null && potentialTransportation.isPresent()) {
                    company.newDelivery(new Delivery(order, company.getAvailableTransportation().get(), company.getHeadquarters(),
                            company.getHeadquarters().toCoordinates(), order.getBuyer().getPostalAddress()));
                }
            }
        }
    }

    private void simulateProductsMovement() {

    }

    private void simulateTransportation() {
        simulateTransportationAcquisitions();
        simulateTransportationDestruction();
    }

    private void simulateCustomersBehavior() {
        //TODO company can gain or lose customers despite the growth signum
        if (economy.getGrowth() > 0) {
            // If economy growth is high, there is a high chance of getting a new customer
            if (random.nextInt(10000) <= economy.getGrowth()) {
                company.newCustomer(DataGenerator.getInstance().generateRandomCustomer());
            }
        } else {
            // If economy growth is negative, there is a chance that our company might lose customers
            if (random.nextInt(10000) <= Math.abs(economy.getGrowth())) {
                company.deleteRandomCustomer();
            }
        }
    }

    private void simulateTransportationAcquisitions() {
        // If orders >= nbr transportation * 100, new transportation acquired
        if (company.getOrders().size() >= company.getAllTransportation().size()*100) {
            // Not instantaneous
            if (random.nextInt(2) == 1000) {
                Transportation transportation = DataGenerator.getInstance().generateRandomTransportation();
                company.newTransportation(transportation);
            }
        }
    }

    private void simulateTransportationDestruction() {
        // TODO
    }



}
