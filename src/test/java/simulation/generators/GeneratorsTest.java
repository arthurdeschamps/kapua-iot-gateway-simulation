package simulation.generators;

import com.github.javafaker.Faker;
import company.address.Address;
import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

/**
 * Created by Arthur Deschamps on 09.06.17.
 */
public class GeneratorsTest {

    @Test
    public void testRandomGenerations() {
        Assert.assertNotNull(AddressGenerator.generateInternationalAddress());
        Assert.assertNotNull(DataGenerator.generateRandomProductType());
        Assert.assertNotNull(AddressGenerator.generateLocalAddress(new Faker().address().cityName()));
    }

    @Test
    public void testLocaleAddressGeneration() {
        Address address = AddressGenerator.generateNationalAddress(new Locale("fr","FR"));
        Assert.assertEquals(address.getCountry(),"France");
    }
}
