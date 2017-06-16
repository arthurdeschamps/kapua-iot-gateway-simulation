package simulator.generator;

import com.github.javafaker.Address;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import company.customer.Customer;
import company.delivery.Delivery;
import company.order.Order;
import company.transportation.PostalAddress;
import company.main.Company;
import company.main.CompanyType;
import company.product.Product;
import company.product.ProductType;
import company.transportation.Transportation;
import company.transportation.TransportationMode;

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

    public static DataGenerator getInstance() {
        return dataGenerator;
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
        return new Product(productType,this.generateRandomAddress().toCoordinates());
    }

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
        if (company != null && company.getProducts().size() > 0 && company.getCustomers().size() > 0) {
            Customer customer = company.getCustomerStore().getRandom();
            List<Product> productList = new ArrayList<>();
            for (int i = 0; i < random.nextInt(10)+1; i++) {
                /*
                 Checks if there is no more product available in the product store. This is done because the method
                 newOrder from Company takes care of removing all the products of an order from the product store
                 before adding the order
                  */
                if (i >= company.getProducts().size())
                    break;
                productList.add(company.getProductStore().getRandom());
            }
            return Optional.of(new Order(customer,productList));
        }

        return Optional.empty();
    }

    public Optional<Delivery> generateRandomDelivery(Company company) {
        if (company.getOrders().size() > 0 && company.getAvailableTransportation().isPresent()) {
            return Optional.of(new Delivery(company.getOrderStore().getRandom(), company.getAvailableTransportation().get(),
                    company.getHeadquarters(), company.getCustomerStore().getRandom().getPostalAddress()));
        }

        return Optional.empty();
    }


    public Transportation generateRandomTransportation() {
        return new Transportation((float) Math.log(Math.random()*10000+1),(int) Math.log(Math.random()*1000),
                TransportationMode.randomTransportationMode());
    }

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
                company.newProduct(this.generateRandomProductFromProductType(productType));
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
