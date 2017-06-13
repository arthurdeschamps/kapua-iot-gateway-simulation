package company.main; /**
 * Created by Arthur Deschamps on 30.05.17.
 */

import company.customer.Customer;
import company.customer.CustomerStore;
import company.transportation.PostalAddress;
import company.delivery.DeliveryStore;
import company.order.Order;
import company.order.OrderStore;
import company.product.Product;
import company.product.ProductStore;
import company.product.ProductType;
import company.product.ProductTypeStore;
import company.transportation.Transportation;
import company.transportation.TransportationMode;
import company.transportation.TransportationStore;
import jedis.JedisManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.GeoCoordinate;
import simulator.generator.CompanyGenerator;
import simulator.generator.DataGenerator;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class CompanyTest {

    private ProductStore productStore;
    private Product expectedProduct;
    private ProductTypeStore productTypeStore;
    private TransportationStore transportationStore;
    private Transportation expectedTransportation;
    private CustomerStore customerStore;
    private OrderStore orderStore;
    private DeliveryStore deliveryStore;
    private static final Logger logger = Logger.getLogger(CompanyTest.class.getName());

    @Before
    public void setUp() throws IOException {
        //TODO test with fake database (to not interfere with app db)
        JedisManager.getInstance().flushDB();

        productTypeStore = new ProductTypeStore();
        productStore = new ProductStore();
        transportationStore = new TransportationStore();
        orderStore = new OrderStore();
        deliveryStore = new DeliveryStore();
        customerStore = new CustomerStore();

        productTypeStore.add(new ProductType("Basket Ball","USA",20.0f,0.6f,false));
        // Create a product with a identical id (name). Should override elder values.
        productTypeStore.add(new ProductType("Basket Ball","France",20.0f,2.0f,false));
        productTypeStore.add(new ProductType("Swiss watch","Switzerland",3450.0f,0.567f,true));

        expectedProduct = new Product(productTypeStore.getStorage().get(0),new GeoCoordinate(100,100));
        productStore.add(expectedProduct);

        expectedTransportation = new Transportation(1000.5f,1348, TransportationMode.WATER);
        transportationStore.add(expectedTransportation);
        transportationStore.add(new Transportation(2.0f,150,TransportationMode.LAND_ROAD));
        transportationStore.add(new Transportation(59.0f,80,TransportationMode.LAND_RAIL));

        customerStore.add(DataGenerator.getInstance().generateRandomCustomer());

    }

    @After
    public void tearDown() {
        JedisManager.getInstance().flushDB();
    }

    @Test
    public void productTypes() {
        Optional<ProductType> watch = productTypeStore.getStorage().stream().filter(productType -> productType.getName().equals("Swiss watch")).findFirst();
        Assert.assertTrue(watch.isPresent());
        final ProductType result = productTypeStore.retrieve(watch.get().getId()).get();
        Assert.assertEquals(watch.get().getName(),result.getName());
        Assert.assertEquals(watch.get().getProductionCountry(),result.getProductionCountry());
        Assert.assertEquals(watch.get().isFragile(),result.isFragile());
        Assert.assertTrue(watch.get().getWeight() == result.getWeight());


        List<ProductType> newProductTypes = productTypeStore.getStorage();
        newProductTypes.removeIf(productType -> !(productType.getName().equals("Basket Ball")));
        // Basketball shall be present twice, since they have a different id
        Assert.assertEquals(2, newProductTypes.size());
        Assert.assertEquals(newProductTypes.get(0).getProductionCountry(),"USA");
    }

    @Test
    public void products() {
        Product notExistingProduct = new Product(productTypeStore.getStorage().get(0),new GeoCoordinate(100,100));
        Assert.assertFalse(productStore.retrieve(notExistingProduct.getId()).isPresent());
        Product result = productStore.retrieve(expectedProduct.getId()).get();
        Assert.assertEquals(expectedProduct.getCurrentLocation(),result.getCurrentLocation());
        Assert.assertTrue(expectedProduct.getPrice() == result.getPrice());
        Assert.assertEquals(expectedProduct.getId(),result.getId());
        Assert.assertEquals(expectedProduct.getProductType(),result.getProductType());
    }

    @Test
    public void transportation() {
        Transportation notExistingTransportation = new Transportation(2.0f,150,TransportationMode.LAND_ROAD);
        // expected shall not be found, since each transportation id is randomly generated
        Assert.assertFalse(transportationStore.retrieve(notExistingTransportation.getId()).isPresent());
        // result is now an existing object
        Transportation result = transportationStore.retrieve(expectedTransportation.getId()).get();
        Assert.assertTrue(expectedTransportation.getCapacity() == result.getCapacity());
        Assert.assertEquals(expectedTransportation.getMaxSpeed(),result.getMaxSpeed());
        Assert.assertEquals(expectedTransportation.getTransportationMode(),result.getTransportationMode());
        Assert.assertTrue(transportationStore.retrieveAll().size() == 3);
    }


    @Test
    public void testEquals() {
        PostalAddress addr1 = new PostalAddress("street","city","region","country","100");
        PostalAddress addr2 = new PostalAddress("street","city","region","country","100");
        Assert.assertEquals(addr1,addr2);
        addr1.setCity("city2");
        Assert.assertNotEquals(addr1,addr2);
        Customer customer1 = new Customer("Arthur","Deschamps",addr1,"arthur@email.com","0000");
        Customer customer2 = new Customer("Arthur","Deschamps",addr1,"arthur@email.com","0000");
        Assert.assertEquals(customer1,customer2);
        customer1.setFirstName("Marc");
        Assert.assertNotEquals(customer1,customer2);
        customer1.setFirstName(customer2.getFirstName());
        addr2.setCountry("USA");
        customer1.setPostalAddress(addr2);
        Assert.assertNotEquals(customer1,customer2);
    }

    @Test
    public void testOrder() {
        List<Product> products = productStore.getStorage();
        List<Customer> customers = customerStore.getStorage();
        orderStore.add(new Order(customers.get(0), productStore.getStorage()));
    }

    @Test
    public void testDelivery() {

    }

    @Test
    public void testConsistency() {
        Company company = new Company(CompanyType.DOMESTIC,"test company",DataGenerator.getInstance().generateRandomAddress());
        company.newProductType(DataGenerator.getInstance().generateRandomProductType());
        company.updateAllData();
        Assert.assertEquals(company.getProductTypes(),JedisManager.getInstance().retrieveAllFromClass(ProductType.class));
    }

    @Test
    public void testGenerateDefault() {
        Company company = new CompanyGenerator().generateDefaultCompany();
        Assert.assertEquals(company.getProducts().size(),JedisManager.getInstance().retrieveAllFromClass(Product.class).size());
        Assert.assertEquals(company.getProducts(),JedisManager.getInstance().retrieveAllFromClass(Product.class));
    }
}
