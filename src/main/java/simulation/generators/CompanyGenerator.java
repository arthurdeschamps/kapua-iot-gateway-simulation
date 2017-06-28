package simulation.generators;

import com.github.javafaker.Faker;
import company.company.Company;
import company.company.CompanyType;
import org.slf4j.LoggerFactory;

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
        LoggerFactory.getLogger(CompanyGenerator.class).info("Generating company...");
        return makeCompany(generateRandomCompanyType());
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

    /**
     * Generates a company of random type without data.
     * @return
     * Newly generated company.
     */
    public static Company generateEmptyRandomCompany() {
        return new Company(generateRandomCompanyType(),generateRandomCompanyName(),AddressGenerator.generateInternationalAddress());
    }

    private static Company makeCompany(CompanyType companyType) {
        Company company = new Company(companyType,generateRandomCompanyName(),AddressGenerator.generateInternationalAddress());
        new DataGenerator(company).generateData();
        return company;
    }

    private static CompanyType generateRandomCompanyType() {
        Random random = new Random();
        int rand = random.nextInt(3);
        switch (rand) {
            case 0:
                return CompanyType.LOCAL;
            case 1:
                return CompanyType.NATIONAL;
            case 2:
                return CompanyType.INTERNATIONAL;
            default:
                return CompanyType.INTERNATIONAL;
        }
    }

    private static String generateRandomCompanyName() {
        return faker.company().name();
    }

}
