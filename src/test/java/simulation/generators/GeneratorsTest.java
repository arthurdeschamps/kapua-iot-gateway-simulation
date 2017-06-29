package simulation.generators;

import com.github.javafaker.Faker;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Arthur Deschamps on 09.06.17.
 */
public class GeneratorsTest {

    private final static Logger logger = LoggerFactory.getLogger(GeneratorsTest.class);

    @Test
    public void testRandomGenerations() {
        Assert.assertNotNull(AddressGenerator.generateInternationalAddress());
        Assert.assertNotNull(DataGenerator.generateRandomProductType());
        Assert.assertNotNull(AddressGenerator.generateLocalAddress(new Faker().address().cityName()));
    }

//    @Test
//    public void testLocaleAddressGeneration() {
//        Address address = AddressGenerator.generateNationalAddress(new Locale("fr","FR"));
//        Assert.assertEquals(address.getCountry(),"France");
//    }
}
