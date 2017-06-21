package simulator.generator;

import com.github.javafaker.Faker;
import company.address.Address;
import company.main.Company;
import company.main.CompanyType;
import company.address.Coordinate;

import java.util.Random;

/**
 * Created by Arthur Deschamps on 02.06.17.
 * TODO: use this class through DataGenerator exclusively
 */
public class CompanyGenerator {

    private String defaultStreet;
    private String defaultCity;
    private String defaultRegion;
    private String defaultCountry;
    private String defaultPostalCode;
    private String defaultCompanyName;
    private Coordinate defaultCoordinate;
    private final CompanyType defaultCompanyType;
    private final Address defaultAddress;

    private static CompanyGenerator companyGenerator = new CompanyGenerator();

    private CompanyGenerator() {
        Faker faker = new Faker();
        com.github.javafaker.Address address = faker.address();
        defaultStreet = address.streetAddress();
        defaultCity = address.city();
        defaultRegion = address.state();
        defaultCountry = address.country();
        defaultPostalCode = address.zipCode();
        defaultCoordinate = new Coordinate(address.latitude(),address.longitude());
        defaultCompanyName = faker.company().name();
        defaultAddress = new Address(defaultStreet,defaultCity,defaultRegion,defaultCountry,defaultPostalCode,defaultCoordinate);
        defaultCompanyType = CompanyType.DOMESTIC;
    }

    public static CompanyGenerator getInstance() {
        return companyGenerator;
    }

    /**
     * Generates a "default" company without any data.
     * @return
     * Object of type Company
     */
    public Company generateDefaultCompanyWithNoData() {
        return new Company(this.defaultCompanyType, this.defaultCompanyName, this.defaultAddress);
    }


    /**
     * Generates a "default" company with random data.
     * @return
     * Object of type Company
     */
    public Company generateDefaultCompany() {
        Company company = generateDefaultCompanyWithNoData();
        DataGenerator.getInstance().generateData(company);
        return company;
    }

    /**
     * Generates a random company with no data.
     * @return
     * Object of type Company
     */
    public Company generateRandomCompanyWithNoData() {
        CompanyType companyType = CompanyType.DOMESTIC;
        Random random = new Random();
        int rand = random.nextInt(3);
        switch (rand) {
            case 0:
                companyType = CompanyType.DOMESTIC;
                break;
            case 1:
                companyType = CompanyType.INTERNATIONAL;
                break;
            case 2:
                companyType = CompanyType.GLOBAL;
                break;
        }
        Faker faker = new Faker();
        return new Company(companyType,faker.company().name(), DataGenerator.getInstance().generateRandomAddress());
    }

    /**
     * Generates a random company with random data.
     * @return
     * Object of type Company
     */
    public Company generateRandomCompany() {
        Company company = generateRandomCompanyWithNoData();
        DataGenerator.getInstance().generateData(company);
        return company;
    }

    public String getDefaultStreet() {
        return defaultStreet;
    }

    public String getDefaultCity() {
        return defaultCity;
    }

    public String getDefaultRegion() {
        return defaultRegion;
    }

    public String getDefaultCountry() {
        return defaultCountry;
    }

    public String getDefaultPostalCode() {
        return defaultPostalCode;
    }

    public String getDefaultCompanyName() {
        return defaultCompanyName;
    }

    public CompanyType getDefaultCompanyType() {
        return defaultCompanyType;
    }

    public Address getDefaultAddress() {
        return defaultAddress;
    }

    public Coordinate getDefaultCoordinate() {
        return defaultCoordinate;
    }

    public void setDefaultCoordinate(Coordinate defaultCoordinate) {
        this.defaultCoordinate = defaultCoordinate;
    }
}
