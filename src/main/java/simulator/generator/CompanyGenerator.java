package simulator.generator;

import com.github.javafaker.Address;
import com.github.javafaker.Faker;
import company.transportation.PostalAddress;
import company.main.Company;
import company.main.CompanyType;

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
    private final CompanyType defaultCompanyType;
    private final PostalAddress defaultPostalAddress;

    public CompanyGenerator() {
        Faker faker = new Faker();
        Address address = faker.address();
        defaultStreet = address.streetAddress();
        defaultCity = address.city();
        defaultRegion = address.state();
        defaultCountry = address.country();
        defaultPostalCode = address.zipCode();
        defaultCompanyName = faker.company().name();
        defaultPostalAddress = new PostalAddress(defaultStreet,defaultCity,defaultRegion,defaultCountry,defaultPostalCode);
        defaultCompanyType = CompanyType.DOMESTIC;
    }

    public Company generateDefaultCompany() {
        Company company = new Company(this.defaultCompanyType, this.defaultCompanyName, this.defaultPostalAddress);
        DataGenerator.getInstance().generateDefaultData(company);
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

    public PostalAddress getDefaultPostalAddress() {
        return defaultPostalAddress;
    }
}
