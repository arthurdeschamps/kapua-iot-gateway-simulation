package simulation.generators;

import com.github.javafaker.Faker;
import company.address.Address;
import company.address.Coordinates;
import company.company.Company;
import company.company.CompanyType;

import java.util.Random;

/**
 * Created by Arthur Deschamps on 02.06.17.
 * TODO: use this class through DataGenerator exclusively
 */
public class CompanyGenerator {

    private final static Faker faker = new Faker();

    /**
     * Generates a "default" company without any data.
     * @return
     * Object of type Company
     */
    public Company generateDefaultCompanyWithNoData() {
        com.github.javafaker.Address address = faker.address();

        final String defaultStreet = address.streetAddress();
        final String defaultCity = address.city();
        final String defaultRegion = address.state();
        final String defaultCountry = address.country();
        final String defaultPostalCode = address.zipCode();
        final Coordinates defaultHeadquartersLocation = new Coordinates(address.latitude(),address.longitude());
        final String defaultCompanyName = faker.company().name();
        final Address defaultAddress = new Address(defaultStreet,defaultCity,defaultRegion,defaultCountry,defaultPostalCode,
                defaultHeadquartersLocation);
        final CompanyType defaultCompanyType = CompanyType.LOCAL;

        return new Company(defaultCompanyType, defaultCompanyName, defaultAddress);
    }


    /**
     * Generates a "default" company with random data.
     * @return
     * Object of type Company
     */
    public Company generateDefaultCompany() {
        Company company = generateDefaultCompanyWithNoData();
        new DataGenerator(company).generateData();
        return company;
    }

    /**
     * Generates a random company with no data.
     * @return
     * Object of type Company
     */
    public Company generateRandomCompanyWithNoData() {
        // Determines random company type
        CompanyType companyType;
        Random random = new Random();
        int rand = random.nextInt(3);
        switch (rand) {
            case 0:
                companyType = CompanyType.LOCAL;
                break;
            case 1:
                companyType = CompanyType.NATIONAL;
                break;
            case 2:
                companyType = CompanyType.INTERNATIONAL;
                break;
            default:
                companyType = CompanyType.INTERNATIONAL;
                break;
        }

        return new Company(companyType,faker.company().name(), AddressGenerator.generateInternationalAddress());
    }

    /**
     * Generates a random company with random data.
     * @return
     * Object of type Company
     */
    public Company generateRandomCompany() {
        Company company = generateRandomCompanyWithNoData();
        new DataGenerator(company).generateData();
        return company;
    }
}
