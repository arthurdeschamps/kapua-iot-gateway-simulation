package simulator;

import com.github.javafaker.Faker;
import company.customer.PostalAddress;
import company.main.Company;
import company.main.CompanyType;

/**
 * Created by Arthur Deschamps on 02.06.17.
 */
class CompanyGenerator {

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
        defaultStreet = faker.address().streetAddress();
        defaultCity = faker.address().city();
        defaultRegion = faker.address().state();
        defaultCountry = faker.address().country();
        defaultPostalCode = faker.address().zipCode();
        defaultCompanyName = faker.company().name();
        defaultPostalAddress = new PostalAddress(defaultStreet,defaultCity,defaultRegion,defaultCountry,defaultPostalCode);
        defaultCompanyType = CompanyType.DOMESTIC;
    }

    Company generateDefault() {
        return new Company(this.defaultCompanyType, this.defaultCompanyName, this.defaultPostalAddress);
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
