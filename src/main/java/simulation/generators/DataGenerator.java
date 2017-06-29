package simulation.generators;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import company.address.Address;
import company.address.Coordinates;
import company.company.Company;
import company.customer.Customer;
import company.delivery.Delivery;
import company.order.Order;
import company.product.Product;
import company.product.ProductType;
import company.transportation.Transportation;
import company.transportation.TransportationMode;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Arthur Deschamps on 01.06.17.
 * Generates default data for simulation
 */
public final class DataGenerator {

    private Company company;
    private static final Random random = new Random();
    private final Faker faker;

    /**
     * DataGenerator allows to create fake data for a company.
     * @param company
     * Company to fill the data with.
     */
    public DataGenerator(Company company) {
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

        // Generate address considering the type of company
        Address address;
        switch (company.getType()) {
            case LOCAL:
                address = AddressGenerator.generateLocalAddress(company.getHeadquarters().getCity());
                break;
            case NATIONAL:
                // TODO: generate a local given the company's address
                address = AddressGenerator.generateNationalAddress(new Locale("en-US"));
                break;
            case INTERNATIONAL:
                address = AddressGenerator.generateInternationalAddress();
                break;
            default:
                address = AddressGenerator.generateInternationalAddress();
                break;
        }

        return new Customer(name.firstName(),name.lastName(), address,
                faker.internet().emailAddress(),faker.phoneNumber().phoneNumber());
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
     * @return
     * A optional object of type Order. If the company doesn't have products or customers, the result is null.
     */
    public Optional<Order> generateRandomOrder() {
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
     * @return
     * The newly generated delivery or an empty optional if the conditions are not met.
     */
    public Optional<Delivery> generateRandomDelivery() {
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
                break;
        }
        return new Transportation(capacity,maxSpeed,transportationMode);
    }

    /**
     * Fills the company with random data (types of product, products, customers, etc).
     * There shall be at least one product type, one product, one customer and one transportation generated.
     */
    void generateData() {
        // Different quantities considering the company's type will be generated.

        generateProductTypes();
        generateProducts();
        generateTransportation();
        generateCustomers();
    }

    private  void generateProductTypes() {
        int nbrProductTypes;
        switch (company.getType()) {
            case LOCAL:
                // From 10 to 20
                nbrProductTypes = random.nextInt(10)+10;
                break;
            case NATIONAL:
                // From 50 to 100
                nbrProductTypes = random.nextInt(50)+50;
                break;
            case INTERNATIONAL:
                // From 100 to 200
                nbrProductTypes = random.nextInt(100)+100;
                break;
            default:
                nbrProductTypes = 20;
                break;
        }

        for (int i = 0; i < nbrProductTypes; i++)
            company.newProductType(generateRandomProductType());
    }

    private  void generateProducts() {
        int nbrProducts;
        switch (company.getType()) {
            case LOCAL:
                nbrProducts = random.nextInt(60)+40;
                break;
            case NATIONAL:
                nbrProducts = random.nextInt(300)+200;
                break;
            case INTERNATIONAL:
                nbrProducts = random.nextInt(1000)+1000;
                break;
            default:
                nbrProducts = 100;
                break;
        }

        for (final ProductType productType : company.getProductTypes()) {
            // Product type base price influences the initial stock (the more expensive, the less quantity)
            for (int i = 0; i < (int) (nbrProducts * (100.0f / productType.getBasePrice())); i++)
                company.newProduct(this.generateProductFromProductType(productType));
        }
    }

    private  void generateTransportation() {
        int nbrTransportation;
        switch (company.getType()) {
            case LOCAL:
                nbrTransportation = random.nextInt(5)+5;
                break;
            case NATIONAL:
                nbrTransportation = random.nextInt(50)+50;
                break;
            case INTERNATIONAL:
                nbrTransportation = random.nextInt(500)+500;
                break;
            default:
                nbrTransportation = 50;
                break;
        }

        for (int i = 0; i < nbrTransportation; i++)
            company.newTransportation(generateRandomTransportation());
    }

    /**
     * Generates customers for the company.
     * This method is delicate in terms of performance and might take long for big amounts of customers.
     */
    private void generateCustomers() {
        int nbrCustomers;
        switch (company.getType()) {
            case LOCAL:
                nbrCustomers = random.nextInt(50)+50;
                break;
            case NATIONAL:
                nbrCustomers = random.nextInt(2000)+700;
                break;
            case INTERNATIONAL:
                nbrCustomers = random.nextInt(5000)+15000;
                break;
            default:
                nbrCustomers = 1000;
                break;
        }

        // We make use of multi-threading in order to speed up the process, which might be very long otherwise.

        final int nbrThreads = 100;
        Set<Customer> customers = new HashSet<>(nbrCustomers);
        final ExecutorService executorService = Executors.newScheduledThreadPool(nbrThreads);
        Collection<Callable<Void>> tasks = new ArrayList<>(nbrCustomers);

        // This callable self-contains anything that it uses (except for the faker for obvious reasons)
        // in order to be as efficient as possible.
        Callable<Void> addUser = () -> {
            final Name name = faker.name();
            final com.github.javafaker.Address fakeAddress = faker.address();
            // Generate address considering the type of company
            final Address address = new Address(fakeAddress.streetAddress(),fakeAddress.city(),fakeAddress.state(),
                    fakeAddress.country(),fakeAddress.zipCode(),new Coordinates(fakeAddress.latitude(),fakeAddress.longitude()));

            customers.add(new Customer(name.firstName(),name.lastName(), address,
                    faker.internet().emailAddress(),faker.phoneNumber().phoneNumber()));
            return null;
        };

        for (int i = 0; i < nbrCustomers; i++)
            tasks.add(addUser);

        try {
            // Executes everything in a blocking way
            executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executorService.shutdown();
        company.getCustomerStore().setStorage(customers);
    }

}
