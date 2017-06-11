package simulator.generator;

import com.github.javafaker.Address;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import company.customer.Customer;
import company.customer.CustomerStore;
import company.transportation.PostalAddress;
import company.main.Company;
import company.main.CompanyType;
import company.delivery.Delivery;
import company.delivery.DeliveryStore;
import company.order.Order;
import company.order.OrderStore;
import company.product.Product;
import company.product.ProductStore;
import company.product.ProductType;
import company.product.ProductTypeStore;
import company.transportation.Transportation;
import company.transportation.TransportationMode;
import company.transportation.TransportationStore;
import jedis.JedisManager;
import redis.clients.jedis.GeoCoordinate;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by Arthur Deschamps on 01.06.17.
 * Generates default data for simulation
 */
public final class DataGenerator {

    private ProductStore productStore;
    private ProductTypeStore productTypeStore;
    private TransportationStore transportationStore;
    private CustomerStore customerStore;
    private OrderStore orderStore;
    private DeliveryStore deliveryStore;
    private Company company;

    private static DataGenerator dataGenerator = new DataGenerator();

    private  final Logger logger = Logger.getLogger(DataGenerator.class.getName());
    private  Faker faker;

    private DataGenerator(){
        faker = new Faker();

        productStore = new ProductStore();
        productTypeStore = new ProductTypeStore();
        transportationStore = new TransportationStore();
        customerStore = new CustomerStore();
        orderStore = new OrderStore();
        deliveryStore = new DeliveryStore();
        company = this.generateRandomCompany();
    }

    public static DataGenerator getInstance() {
        return dataGenerator;
    }

    public  void generateDefaultDatabase() {
        deleteAll();
        generateDatabase();
    }

    public  Customer generateRandomCustomer() {
        Name name = faker.name();
        return new Customer(name.firstName(),name.lastName(), this.generateRandomAddress(),
                faker.internet().emailAddress(),faker.phoneNumber().phoneNumber());
    }

    PostalAddress generateRandomAddress() {
        if (productStore == null)
            logger.info("productStore null");
        Address address = faker.address();
        return new PostalAddress(address.streetAddress(),address.cityName(),address.state(),address.country(),
                address.zipCode());
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

    private  void generateDatabase() {
        logger.info("Flushing database...");
        deleteAll();
        logger.info("Done");
        logger.info("Populating database...");
        generateProductTypes();
        generateProducts();
        generateTransportation();
        generateCustomers();
        generateOrders();
        generateDeliveries();
        logger.info("All done !");
    }

    private  void generateProductTypes() {
        productTypeStore.add(new ProductType("Apple","Germany",0.5f,0.2f,false));
        productTypeStore.add(new ProductType("Diamond","Brasil",1000.0f,1.4f,true));
        productTypeStore.add(new ProductType("Basket ball","USA",30.0f,1.0f,false));
        productTypeStore.add(new ProductType("Crystal glass","France",198.4f,1.5f,true));
        productTypeStore.add(new ProductType("Sony QLED TV","Japan",4509.99f,20.4f,true));
    }

    private  void generateProducts() {
        GeoCoordinate coordinate = new GeoCoordinate(100,100);
        Product apple;
        for (int i = 0; i < 20000; i++) {
            apple = new Product(productTypeStore.getStorage().get(0),coordinate);
            productStore.add(apple);
        }
        Product basketBall;
        for (int i = 0; i < 100; i++) {
            basketBall = new Product(productTypeStore.getStorage().get(3),coordinate,20);
            productStore.add(basketBall);
        }
        Product tv;
        for (int i = 0; i < 450; i++) {
            tv = new Product(productTypeStore.getStorage().get(4),coordinate,2499);
            productStore.add(tv);
        }
    }

    private  void generateTransportation() {
        transportationStore.add(new Transportation(450,60, TransportationMode.LAND_RAIL));
        transportationStore.add(new Transportation(259,140,TransportationMode.LAND_ROAD));
        transportationStore.add(new Transportation(30000,30,TransportationMode.WATER));
    }

    private  void generateCustomers() {
        for (int i = 0; i < 10; i++)
            customerStore.add(dataGenerator.generateRandomCustomer());
    }

    private  void generateOrders() {
        List<Product> products = productStore.getStorage();
        Random randomUtil = new Random();
        for (final Customer customer : customerStore.getStorage()) {
            for (int j = 0; j < randomUtil.nextInt(5)+1; j++) {
                Order order = new Order(customer);
                final int nbrProducts = products.size();
                for (int i = 0; i < randomUtil.nextInt(10)+1; i++)
                    order.getOrderedProducts().add(products.get(randomUtil.nextInt(nbrProducts)));
                orderStore.add(order);
            }
        }
    }

    private  void generateDeliveries() {
        Random randomUtil = new Random();
        for (final Customer customer : customerStore.getStorage()) {
            final int size = transportationStore.getStorage().size();
            deliveryStore.add(new Delivery(orderStore.getByCustomer(customer),transportationStore.getStorage().get(randomUtil.nextInt(size)),company.getHeadquarters(),
                    company.getHeadquarters().toCoordinates(),customer.getPostalAddress()));
        }
    }

    private  void deleteAll() {
        JedisManager.getInstance().flushDB();
    }
}
