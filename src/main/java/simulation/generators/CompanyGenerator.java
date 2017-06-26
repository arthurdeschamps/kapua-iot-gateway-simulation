package simulation.generators;

import com.github.javafaker.Faker;
import company.company.Company;
import company.company.CompanyType;

import java.util.Random;

/**
 * Generates different kind of companies.
 * @author Arthur Deschamps
 * @since 1.0
 * @see Company
 */
public class CompanyGenerator {

    private final static Faker faker = new Faker();

    /**
     * Generates a random company with random data.
     * @return
     * Object of type Company
     */
    public static Company generateRandomCompany() {
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
        return makeCompany(companyType);
    }

    /**
     * Generates a company of type local.
     * @return
     * Newly generated company.
     */
    public static Company generateLocalCompany() {
        return makeCompany(CompanyType.LOCAL);
    }

    /**
     * Generates a company of type national.
     * @return
     * Newly generated company.
     */
    public static Company generateNationalCompany() {
        return makeCompany(CompanyType.NATIONAL);
    }

    /**
     * Generates a company of type international.
     * @return
     * Newly generated company.
     */
    public static Company generateInternationalCompany() {
        return makeCompany(CompanyType.INTERNATIONAL);
    }

    private static Company makeCompany(CompanyType companyType) {
        Company company = new Company(companyType,faker.company().name(),AddressGenerator.generateInternationalAddress());
        new DataGenerator(company).generateData();
        return company;
    }

}
