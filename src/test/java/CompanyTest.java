/**
 * Created by Arthur Deschamps on 30.05.17.
 */

import company.*;
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
    private TransportationStore transportationStore;
    private Transportation expectedTransportation;
    private final Logger logger = Logger.getLogger(CompanyTest.class.getName());

    @Before
    public void setUp() throws IOException {
        //TODO test database server (to not interfere in app db)
        JedisManager.getInstance().getResource().flushDB();

        productStore = new ProductStore();
        transportationStore = new TransportationStore();

        productStore.add(new Product("Basket Ball","USA",20.0f,0.6f,false));
        // Create a product with a identical id (name). Should override elder values.
        productStore.add(new Product("Basket Ball","France",20.0f,2.0f,false));
        productStore.add(new Product("Swiss watch","Switzerland",3450.0f,0.567f,true));

        expectedTransportation = new Transportation(1000.5f,new GeoCoordinate(43.6666,32.09),1348, TransportationMode.WATER);
        transportationStore.add(expectedTransportation);
        transportationStore.add(new Transportation(2.0f,new GeoCoordinate(1390.94,-3338.46),150,TransportationMode.LAND_ROAD));
        transportationStore.add(new Transportation(59.0f,new GeoCoordinate(12.339,-0.9),80,TransportationMode.LAND_RAIL));
    }

    @After
    public void tearDown() {
    }

    @Test
    public void products() {
        Product watch = new Product("Swiss watch","Switzerland",3450.0f,0.567f,true);
        if (!productStore.retrieve(watch.getId()).isPresent())
            Assert.fail();
        final Product result = productStore.retrieve(watch.getId()).get();
        Assert.assertEquals(watch.getName(),result.getName());
        Assert.assertEquals(watch.getProductionCountry(),result.getProductionCountry());
        Assert.assertEquals(watch.isFragile(),result.isFragile());
        Assert.assertTrue(watch.getWeight() == result.getWeight());


        List<Product> newProducts = productStore.getStorage();
        newProducts.removeIf(product -> !(product.getName().equals("Basket Ball")));
        Assert.assertEquals(1,newProducts.size());
        Assert.assertEquals(newProducts.get(0).getProductionCountry(),"USA");
    }

    @Test
    public void transportation() {
        Transportation notExistingTransportation = new Transportation(2.0f,new GeoCoordinate(1390.94,-3338.46),150,TransportationMode.LAND_ROAD);
        // expected shall not be found, since each transportation id is randomly generated
        if (transportationStore.retrieve(notExistingTransportation.getId()).isPresent())
            Assert.fail();
        // result is now an existing object
        Transportation result = transportationStore.retrieve(expectedTransportation.getId()).get();
        Assert.assertTrue(expectedTransportation.getCapacity() == result.getCapacity());
        Assert.assertEquals(expectedTransportation.getCurrentPosition(),result.getCurrentPosition());
        Assert.assertEquals(expectedTransportation.getMaxSpeed(),result.getMaxSpeed());
        Assert.assertEquals(expectedTransportation.getTransportationMode(),result.getTransportationMode());
        Assert.assertTrue(transportationStore.retrieveAll().size() == 3);
    }
}
