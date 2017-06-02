/**
 * Created by Arthur Deschamps on 30.05.17.
 */

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

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class CompanyTest {

    private ProductStore productStore;
    private Product expectedProduct;
    private ProductTypeStore productTypeStore;
    private TransportationStore transportationStore;
    private Transportation expectedTransportation;
    private final Logger logger = Logger.getLogger(CompanyTest.class.getName());

    @Before
    public void setUp() throws IOException {
        //TODO test database server (to not interfere in app db)
        JedisManager.getInstance().getResource().flushDB();

        productTypeStore = new ProductTypeStore();
        productStore = new ProductStore();
        transportationStore = new TransportationStore();

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
    }

    @After
    public void tearDown() {
    }

    @Test
    public void productTypes() {
        ProductType watch = new ProductType("Swiss watch","Switzerland",3450.0f,0.567f,true);
        Assert.assertTrue(productTypeStore.retrieve(watch.getId()).isPresent());
        final ProductType result = productTypeStore.retrieve(watch.getId()).get();
        Assert.assertEquals(watch.getName(),result.getName());
        Assert.assertEquals(watch.getProductionCountry(),result.getProductionCountry());
        Assert.assertEquals(watch.isFragile(),result.isFragile());
        Assert.assertTrue(watch.getWeight() == result.getWeight());


        List<ProductType> newProductTypes = productTypeStore.getStorage();
        newProductTypes.removeIf(productType -> !(productType.getName().equals("Basket Ball")));
        Assert.assertEquals(1, newProductTypes.size());
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
}
