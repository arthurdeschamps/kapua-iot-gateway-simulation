package simulator.generator;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import company.address.Address;
import company.address.Coordinates;
import company.customer.Customer;
import company.delivery.Delivery;
import company.main.Company;
import company.order.Order;
import company.product.Product;
import company.product.ProductType;
import company.transportation.Transportation;
import company.transportation.TransportationMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Created by Arthur Deschamps on 01.06.17.
 * Generates default data for simulation
 */
public final class DataGenerator {

    private Company company;
    private static final Random random = new Random();
    private  Faker faker;

    /**
     * DataGenerator allows to create fake data for a company.
     * @param company
     * Company to fill the data with.
     */
    public DataGenerator(Company company){
        this.company = company;
        this.faker = new Faker();
    }

    /**
     * Generates a random customer.
     * @return
     * The newly generated customer.
     */
    public Customer generateRandomCustomer() {
        Name name = faker.name();
        return new Customer(name.firstName(),name.lastName(), generateRandomAddress(),
                faker.internet().emailAddress(),faker.phoneNumber().phoneNumber());
    }

    /**
     * Generates a random address.
     * @return
     * The newly generated address.
     */
    public static Address generateRandomAddress() {
        Faker faker = new Faker();
        com.github.javafaker.Address address = faker.address();
        return new Address(address.streetAddress(),address.cityName(),address.state(),address.country(),
                address.zipCode(),new Coordinates(address.latitude(),address.longitude()));
    }

    /**
     * Generates a product with the given product type and the coordinates contained in the company address.
     * @param productType
     * The type of product to be generated.
     * @return
     * The newly generated product.
     */
    public Product generateProductFromProductType(ProductType productType) {
        return new Product(productType,company.getHeadquarters().getCoordinates());
    }

    /**
     * Generates a random type of product (random name, production country, price, etc).
     * @return
     * The newly generated type of product.
     */
    public static ProductType generateRandomProductType() {
        Faker faker = new Faker();
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
    public static Transportation generateRandomTransportation() {
        TransportationMode transportationMode = TransportationMode.randomTransportationMode();
        int maxSpeed;
        float capacity;
        switch (transportationMode) {
            case WATER:
                maxSpeed = (int) (Math.random()*20 + 30);
                capacity = (int) (Math.random()*15000 + 10000);
                break;
            case LAND_ROAD:
                maxSpeed = (int) (Math.random()*50 + 100);
                capacity = (int) (Math.random()*5000+2000);
                break;
            case LAND_RAIL:
                maxSpeed = (int) (Math.random()*200 + 150);
                capacity = (int) (Math.random()*5000 + 5000);
                break;
            case AIR:
                maxSpeed = (int) (Math.random()*100 + 350);
                capacity = (int) (Math.random()*3000 + 1000);
                break;
            default:
                capacity = 500;
                maxSpeed = 200;
        }
        return new Transportation(capacity,maxSpeed,transportationMode);
    }

    /**
     * Fills the company with random data (types of product, products, customers, etc).
     * There shall be at least one product type, one product, one customer and one transportation generated.
     */
    void generateData() {
        // TODO: generate regarding to the company business type
        generateProductTypes();
        generateProducts();
        generateTransportation();
        generateCustomers();
    }

    private  void generateProductTypes() {
        for (int i = 0; i < random.nextInt(10)+1; i++)
            company.newProductType(generateRandomProductType());
    }

    private  void generateProducts() {
        for (final ProductType productType : company.getProductTypes())
            for (int i = 0; i < random.nextInt(100)+1; i++)
                company.newProduct(this.generateProductFromProductType(productType));
    }

    private  void generateTransportation() {
        for (int i = 0; i < random.nextInt(50)+1; i++)
            company.newTransportation(generateRandomTransportation());
    }

    private  void generateCustomers() {
        for (int i = 0; i < random.nextInt(50)+1; i++)
            company.newCustomer(generateRandomCustomer());
    }

}
