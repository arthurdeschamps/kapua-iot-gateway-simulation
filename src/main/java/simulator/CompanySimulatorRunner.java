package simulator;

import company.delivery.Delivery;
import company.main.Company;
import company.order.Order;
import company.product.Product;
import company.product.ProductType;
import company.transportation.Transportation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by Arthur Deschamps on 06.06.17.
 */
public class CompanySimulatorRunner implements Runnable {

    private final Logger logger = Logger.getLogger(CompanySimulatorRunner.class.getName());

    private Company company;
    private EconomySimulatorRunner economy;
    private Random random;

    CompanySimulatorRunner(Company company, EconomySimulatorRunner economy) {
        this.company = company;
        this.economy = economy;
        random = new Random();
    }

    @Override
    public void run() {
        try {
            simulateProductTypeCreation();
            simulateProduction();
            simulateProductTypeDestruction();
            simulateCustomersBehavior();
            simulateNewOrders();
            simulateDeliveries();
            simulateProductsMovement();
            simulatePriceCuts();
            simulateTransportationAcquisitions();
            simulateTransportationDestruction();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            logger.info("New product type: "+productType.getName());
        }
    }

    private void simulateProductTypeDestruction() {
        int productQuantity;
        for (final ProductType productType : company.getProductTypes()) {

            // Sometimes when stock is down to 0 and no order on the product's type exist, company decides to get rid of it
            // Company must have at least 2 types of products
            productQuantity = (int) company.getProductQuantity(productType);
            if (productQuantity == 0 && company.getOrdersForProductType(productType).size() == 0 && company.getProductTypes().size() > 2) {
                if (random.nextInt(1000) == 0) {
                    company.deleteProductType(productType);
                    logger.info("Product type " + productType.getName() + " deleted");
                    return;
                }
            }
        }
    }

    private void simulatePriceCuts() {
        // TODO
    }

    private void simulateNewOrders() {
        // No customer means no order
        if (company.getCustomers().size() > 0 && company.getProducts().size() > 0) {
            // A product has probability nbrCustomers/(price*10^8) to be ordered
            int nbrOutcomes;
            List<Product> orderedProducts = new ArrayList<>();
            for (final Product product : company.getProducts()) {
                nbrOutcomes = (int) (product.getPrice() * Math.pow(10, 5)) / company.getCustomers().size();
                if (nbrOutcomes < 1)
                    nbrOutcomes = 1;

                if (random.nextInt(nbrOutcomes) == 0) {
                    logger.info("Product \"" + product.getProductType().getName() + "\" ordered");
                    orderedProducts.add(product);
                }
            }
            if (orderedProducts.size() > 0) {
                company.newOrder(new Order(company.getCustomers().get(random.nextInt(company.getCustomers().size())), orderedProducts));
                logger.info("New order");
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
                    logger.info("New delivery for order no. " + order.getId());
                }
            }
        }
    }

    private void simulateProductsMovement() {}

    private void simulateCustomersBehavior() {
        //TODO company can gain or lose customers despite the growth signum
        if (economy.getGrowth() > 0) {
            // If economy growth is high, there is a high chance of getting a new customer
            if (random.nextInt(10000) <= economy.getGrowth()) {
                company.newCustomer(DataGenerator.getInstance().generateRandomCustomer());
                logger.info("New customer");
            }
        } else {
            // If economy growth is negative, there is a chance that our company might lose customers
            if (random.nextInt(10000) <= Math.abs(economy.getGrowth())) {
                if(company.deleteRandomCustomer())
                    logger.info("Customer lost");
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
                logger.info("New transportation of type "+transportation.getTransportationMode().name());
            }
        }
    }

    private void simulateTransportationDestruction() {
        // TODO
    }



}
