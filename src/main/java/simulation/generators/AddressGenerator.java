package simulation.generators;

import company.address.Address;
import company.address.Coordinates;

import java.util.Locale;

/**
 * Address generator
 * @since 1.0
 * @author Arthur Deschamps
 */
class AddressGenerator {

    /**
     * Generates a random address with the given Faker. Faker can use a Local object to generate more specific addresses.
     * @return
     * The newly generated address.
     */
    private static Address generateRandomAddress() {
        com.github.javafaker.Address address = Faker.getInstance().address();
        return new Address(address.streetAddress(),address.cityName(),address.state(),address.country(),
                address.zipCode(),new Coordinates(address.latitude(),address.longitude()));
    }

    /**
     * Generates an address within @city.
     * @param city
     * The city to generate the address within
     * @return
     * An address in the given city.
     */
    static Address generateLocalAddress(String city) {
        //TODO
        return generateRandomAddress();
    }

    /**
     * Generates an address within @country.
     * @param locale
     * The geographical context (country) to generate the address within.
     * @return
     * An address in the given country.
     */
    static Address generateNationalAddress(Locale locale) {
        return generateRandomAddress();
    }

    /**
     * Generates an address anywhere in the world
     * @return
     * An address.
     */
    static Address generateInternationalAddress() {
        return generateRandomAddress();
    }
}
