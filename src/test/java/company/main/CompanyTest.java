package company.main;

import company.delivery.Delivery;
import company.order.Order;
import company.product.ProductType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import simulator.generator.CompanyGenerator;
import simulator.generator.DataGenerator;

import java.util.Optional;

/**
 * Created by Arthur Deschamps on 30.05.17.
 */

public class CompanyTest {

    private static Company company;

    @BeforeClass
    public static void setUp() {
        company = CompanyGenerator.getInstance().generateRandomCompanyWithNoData();
        Assert.assertNotNull(company);
    }

    @Test
    public void testNew() {
        DataGenerator dataGenerator = DataGenerator.getInstance();
        company.newCustomer(dataGenerator.generateRandomCustomer());
        Assert.assertEquals(1,company.getCustomers().size());
        company.newProductType(dataGenerator.generateRandomProductType());
        Assert.assertEquals(1,company.getProductTypes().size());
        company.newProduct(dataGenerator.generateRandomProductFromProductType(company.getProductTypes().iterator().next()));
        Assert.assertEquals(1,company.getProducts().size());
        Optional<Order> order = dataGenerator.generateRandomOrder(company);
        if (!order.isPresent())
            Assert.fail("DataGenerator couldn't generate a random order. Company may not have any customer or product.");
        company.newOrder(order.get());
        Assert.assertEquals(1,company.getOrders().size());
        Assert.assertEquals(0,company.getProducts().size());
        company.newTransportation(dataGenerator.generateRandomTransportation());
        Assert.assertEquals(1,company.getAllTransportation().size());
        Assert.assertNotNull(company.getAvailableTransportation());
        Optional<Delivery> delivery = dataGenerator.generateRandomDelivery(company);
        if (!delivery.isPresent())
            Assert.fail("DataGenerator couldn't generate a random delivery. Company may no have any order or no available transportation.");
        company.newDelivery(delivery.get());
        Assert.assertEquals(1,company.getDeliveries().size());
        Assert.assertEquals(0,company.getOrders().size());
    }

    @Test
    public void testStores() {
        Assert.assertEquals(company.getProducts(),company.getProductStore().getStorage());
        Assert.assertEquals(company.getAllTransportation(),company.getTransportationStore().getStorage());
    }

    @Test
    public void testUtilityMethods() {
        // Test product quantity
        ProductType productType = DataGenerator.getInstance().generateRandomProductType();
        company.newProductType(productType);
        final int initialSize = company.getProducts().size();
        company.newProduct(DataGenerator.getInstance().generateRandomProductFromProductType(productType));
        company.newProduct(DataGenerator.getInstance().generateRandomProductFromProductType(productType));
        Assert.assertEquals(initialSize+2,company.getProducts().size());
        Assert.assertEquals(2,company.getProductQuantity(productType));

        // Test deletes/remove
        company.deleteProductType(productType);
        Assert.assertFalse(company.getProductTypes().contains(productType));
        Assert.assertEquals(0,company.getProductQuantity(productType));

        final int initialCustomerSize = company.getCustomers().size();
        company.newCustomer(DataGenerator.getInstance().generateRandomCustomer());
        Assert.assertEquals(initialCustomerSize+1,company.getCustomers().size());
        company.deleteRandomCustomer();
        Assert.assertEquals(initialCustomerSize,company.getCustomers().size());

        // Test get available transportation
        company.getAllTransportation().removeIf(transportation -> true); // removes all transportation
        company.newTransportation(DataGenerator.getInstance().generateRandomTransportation());
        company.newProductType(DataGenerator.getInstance().generateRandomProductType());
        company.newProduct(DataGenerator.getInstance().generateRandomProductFromProductType(
                company.getProductTypeStore().getRandom()
        ));
        company.newCustomer(DataGenerator.getInstance().generateRandomCustomer());
        DataGenerator.getInstance().generateRandomOrder(company).ifPresent(order ->
            company.newOrder(order)
        );
        Assert.assertNotEquals(0,company.getOrders().size());
        // Creates a new delivery to assign our unique transportation
        DataGenerator.getInstance().generateRandomDelivery(company).ifPresent(delivery ->
                company.newDelivery(delivery)
        );
        Assert.assertNotEquals(0,company.getDeliveries().size());
        // Now available transportation should be empty
        Assert.assertFalse(company.getAvailableTransportation().isPresent());
    }
}
