package simulator.generator;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import company.customer.Customer;
import company.delivery.Delivery;
import company.order.Order;
import company.address.Address;
import company.main.Company;
import company.product.Product;
import company.product.ProductType;
import company.transportation.Transportation;
import company.transportation.TransportationMode;
import company.address.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    /**
     * Returns the unique instance of the class.
     * @return
     * An instance of DataGenerator.
     */
    public static DataGenerator getInstance() {
        return dataGenerator;
    }

    /**
     * Generates a random customer.
     * @return
     * The newly generated customer.
     */
    public  Customer generateRandomCustomer() {
        Name name = faker.name();
        return new Customer(name.firstName(),name.lastName(), this.generateRandomAddress(),
                faker.internet().emailAddress(),faker.phoneNumber().phoneNumber());
    }

    /**
     * Generates a random address.
     * @return
     * The newly generated address.
     */
    public Address generateRandomAddress() {
        com.github.javafaker.Address address = faker.address();
        return new Address(address.streetAddress(),address.cityName(),address.state(),address.country(),
                address.zipCode(),new Coordinate(address.latitude(),address.longitude()));
    }

    /**
     * Generates a product with the given product type and the coordinate contained in the company address.
     * @param companyAddress
     * The address of the company.
     * @param productType
     * The type of product to be generated.
     * @return
     * The newly generated product.
     */
    public Product generateProductFromProductType(Address companyAddress, ProductType productType) {
        return new Product(productType,companyAddress.getCoordinate());
    }

    /**
     * Generates a random type of product (random name, production country, price, etc).
     * @return
     * The newly generated type of product.
     */
    public ProductType generateRandomProductType() {
        return new ProductType(faker.commerce().productName(),faker.address().country(),
                (float) Math.random()*1000, (float) Math.log(Math.random()*10000+1), faker.bool().bool());
    }

    /**
     * Returns a randomly generated order
     * @param company
     * An object of type Company is required to be able to access the products and customers.
     * @return
     * A optional object of type Order. If the company doesn't have products or customers, the result is null.
     */
    public Optional<Order> generateRandomOrder(Company company) {
        if (company != null && company.getProductStore().getRandom().isPresent() && company.getCustomerStore().getRandom().isPresent()) {
            Customer customer = company.getCustomerStore().getRandom().get();
            List<Product> productList = new ArrayList<>();
            for (int i = 0; i < random.nextInt(10)+1; i++) {
                /*
                 Checks if there is no more product available in the product store. This is done because the method
                 newOrder from Company takes care of removing all the products of an order from the product store
                 before adding the order
                  */
                if (i >= company.getProducts().size())
                    break;
                productList.add(company.getProductStore().getRandom().get());
            }
            return Optional.of(new Order(customer,productList));
        }

        return Optional.empty();
    }

    /**
     * Generates a random delivery if the number of orders of the company is strictly greater than 0 and there is
     * an available transportation.
     * @param company
     * Company to generate the delivery for.
     * @return
     * The newly generated delivery or an empty optional if the conditions are not met.
     */
    public Optional<Delivery> generateRandomDelivery(Company company) {
        if (company.getOrders().size() > 0 && company.getAvailableTransportation().isPresent() &&
                company.getCustomerStore().getRandom().isPresent() && company.getOrderStore().getRandom().isPresent()) {
            return Optional.of(new Delivery(company.getOrderStore().getRandom().get(), company.getAvailableTransportation().get(),
                    company.getHeadquarters(), company.getCustomerStore().getRandom().get().getAddress()));
        }

        return Optional.empty();
    }


    /**
     * Generates a random transportation.
     * @return
     * The newly generated transportation.
     */
    public Transportation generateRandomTransportation() {
        return new Transportation((float) Math.log(Math.random()*10000+1),(int) Math.log(Math.random()*1000),
                TransportationMode.randomTransportationMode());
    }

    /**
     * Fills the company with random data (types of product, products, customers, etc).
     * @param company
     * The company to be filled with data.
     */
    void generateData(Company company) {
        // TODO: generate regarding to the company business type
        generateProductTypes(company);
        generateProducts(company);
        generateTransportation(company);
        generateCustomers(company);
    }

    private  void generateProductTypes(Company company) {
        for (int i = 0; i < random.nextInt(10); i++)
            company.newProductType(this.generateRandomProductType());
    }

    private  void generateProducts(Company company) {
        for (final ProductType productType : company.getProductTypes())
            for (int i = 0; i < random.nextInt(100); i++)
                company.newProduct(this.generateProductFromProductType(company.getHeadquarters(),productType));
    }

    private  void generateTransportation(Company company) {
        for (int i = 0; i < random.nextInt(50); i++)
            company.newTransportation(this.generateRandomTransportation());
    }

    private  void generateCustomers(Company company) {
        for (int i = 0; i < random.nextInt(50); i++)
            company.newCustomer(this.generateRandomCustomer());
    }

}
