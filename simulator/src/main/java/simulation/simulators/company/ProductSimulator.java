package simulation.simulators.company;

import company.company.Company;
import company.product.Product;
import company.product.ProductType;
import economy.Economy;
import simulation.generators.DataGenerator;
import simulation.util.ProbabilityUtils;

import java.util.Random;

/**
 * Simulates everything related to products and product types
 * @since 1.0
 * @author Arthur Deschamps
 */
public class ProductSimulator extends AbstractCompanyComponentSimulator {

    public ProductSimulator(Company company, Economy economy) {
        super(company, economy);
    }

    @Override
    public void run() {
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
            if (probabilityUtils.event(2, ProbabilityUtils.TimeUnit.DAY)) {
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
        if (probabilityUtils.event(Math.abs(economy.getGrowth()-economy.getDemand()+economy.getSectorConcurrency()), ProbabilityUtils.TimeUnit.WEEK)) {
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
                if (probabilityUtils.event(2, ProbabilityUtils.TimeUnit.WEEK)) {
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
            if (probabilityUtils.event(company.getOrdersFromProductType(productType).size()/100, ProbabilityUtils.TimeUnit.MONTH)) {
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
}
