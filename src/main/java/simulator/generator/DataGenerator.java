package simulator.generator;

import com.github.javafaker.Address;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import company.customer.Customer;
import company.transportation.PostalAddress;
import company.main.Company;
import company.main.CompanyType;
import company.product.Product;
import company.product.ProductStore;
import company.product.ProductType;
import company.transportation.Transportation;
import company.transportation.TransportationMode;
import jedis.JedisManager;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by Arthur Deschamps on 01.06.17.
 * Generates default data for simulation
 */
public final class DataGenerator {

    private static DataGenerator dataGenerator = new DataGenerator();

    private  final Logger logger = Logger.getLogger(DataGenerator.class.getName());
    private static final Random random = new Random();
    private  Faker faker;

    private DataGenerator(){
        faker = new Faker();
    }

    public static DataGenerator getInstance() {
        return dataGenerator;
    }

    void generateDefaultData(Company company) {
        deleteAll();
        generateData(company);
    }

    public  Customer generateRandomCustomer() {
        Name name = faker.name();
        return new Customer(name.firstName(),name.lastName(), this.generateRandomAddress(),
                faker.internet().emailAddress(),faker.phoneNumber().phoneNumber());
    }

    public PostalAddress generateRandomAddress() {
        Address address = faker.address();
        return new PostalAddress(address.streetAddress(),address.cityName(),address.state(),address.country(),
                address.zipCode());
    }

    public Product generateRandomProductFromProductType(ProductType productType) {
        // TODO generate different price from stock one sometimes ?
        return new Product(productType,this.generateRandomAddress().toCoordinates());
    }

    public ProductType generateRandomProductType() {
        return new ProductType(faker.commerce().productName(),faker.address().country(),
                (float) Math.random()*1000, (float) Math.log(Math.random()*10000+1), faker.bool().bool());
    }

    private  Company generateRandomCompany() {
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
        return new Company(companyType,faker.company().name(), this.generateRandomAddress());
    }

    public Transportation generateRandomTransportation() {
        return new Transportation((float) Math.log(Math.random()*10000+1),(int) Math.log(Math.random()*1000),
                TransportationMode.randomTransportationMode());
    }

    private void generateData(Company company) {
        // TODO: generate regarding to the company business type
        logger.info("Flushing database...");
        deleteAll();
        logger.info("Done");
        logger.info("Populating database...");
        generateProductTypes(company);
        generateProducts(company);
        generateTransportation(company);
        generateCustomers(company);
        logger.info("All done !");
    }

    private  void generateProductTypes(Company company) {
        for (int i = 0; i < random.nextInt(10); i++)
            company.newProductType(this.generateRandomProductType());
    }

    private  void generateProducts(Company company) {
        for (final ProductType productType : company.getProductTypes()) {
            for (int i = 0; i < random.nextInt(100); i++) {
                company.newProduct(this.generateRandomProductFromProductType(productType));
            }
        }
    }

    private  void generateTransportation(Company company) {
        for (int i = 0; i < random.nextInt(50); i++)
            company.newTransportation(this.generateRandomTransportation());
    }

    private  void generateCustomers(Company company) {
        for (int i = 0; i < random.nextInt(50); i++)
            company.newCustomer(this.generateRandomCustomer());
    }

    private  void deleteAll() {
        JedisManager.getInstance().flushAll();
    }
}
