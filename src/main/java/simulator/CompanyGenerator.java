package simulator;

import company.customer.PostalAddress;
import company.main.Company;
import company.main.CompanyType;

/**
 * Created by Arthur Deschamps on 02.06.17.
 */
class CompanyGenerator {

    private final String defaultStreet = "Default street";
    private final String defaultCity = "Default city";
    private final String defaultRegion = "Default region";
    private final String defaultCountry = "Default country";
    private final int defaultPostalCode = 1000;
    private final String defaultCompanyName = "Default company";
    private final CompanyType defaultCompanyType = CompanyType.DOMESTIC;

    private final PostalAddress defaultPostalAddress = new PostalAddress(defaultStreet,defaultCity,defaultRegion,defaultCountry,defaultPostalCode);

    public CompanyGenerator() {}

    Company generateDefault() {
        return new Company(this.defaultCompanyType, this.defaultCompanyName, this.defaultPostalAddress);
    }
}
