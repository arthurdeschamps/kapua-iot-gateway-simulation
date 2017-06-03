package simulator;

import com.sun.tools.corba.se.idl.constExpr.Or;
import company.customer.Customer;
import company.customer.CustomerStore;
import company.customer.PostalAddress;
import company.main.Company;
import company.main.CompanyType;
import company.order.Delivery;
import company.order.DeliveryStore;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by Arthur Deschamps on 01.06.17.
 * Generates default data for simulation
 */
public class PopulateDatabase {

    private static ProductStore productStore = new ProductStore();
    private static ProductTypeStore productTypeStore = new ProductTypeStore();
    private static TransportationStore transportationStore = new TransportationStore();
    private static CustomerStore customerStore = new CustomerStore();
    private static OrderStore orderStore = new OrderStore();
    private static DeliveryStore deliveryStore = new DeliveryStore();
    private static Company company = new Company(CompanyType.DOMESTIC,"Iot Corp",new PostalAddress("4 Goldfield Rd","Honolulu","HI","United States",96815));
    private static final Logger logger = Logger.getLogger(PopulateDatabase.class.getName());

    public static void main(String[] args) {
        generateDatabase();
    }

    private static void generateDatabase() {
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

    private static void generateProductTypes() {
        productTypeStore.add(new ProductType("Apple","Germany",0.5f,0.2f,false));
        productTypeStore.add(new ProductType("Diamond","Brasil",1000.0f,1.4f,true));
        productTypeStore.add(new ProductType("Basket ball","USA",30.0f,1.0f,false));
        productTypeStore.add(new ProductType("Crystal glass","France",198.4f,1.5f,true));
        productTypeStore.add(new ProductType("Sony QLED TV","Japan",4509.99f,20.4f,true));
    }

    private static void generateProducts() {
        GeoCoordinate coordinate = new GeoCoordinate(100,100);
        Product apple = new Product(productTypeStore.getStorage().get(0),coordinate);
        for (int i = 0; i < 20000; i++)
            productStore.add(apple);
        Product basketBall = new Product(productTypeStore.getStorage().get(3),coordinate,20);
        for (int i = 0; i < 100; i++)
            productStore.add(basketBall);
        Product tv = new Product(productTypeStore.getStorage().get(4),coordinate,2499);
        for (int i = 0; i < 450; i++) {
            productStore.add(tv);
        }
    }

    private static void generateTransportation() {
        transportationStore.add(new Transportation(450,60, TransportationMode.LAND_RAIL));
        transportationStore.add(new Transportation(259,140,TransportationMode.LAND_ROAD));
        transportationStore.add(new Transportation(30000,30,TransportationMode.WATER));
    }

    private static void generateCustomers() {
        customerStore.add(new Customer("Dave","Johnson",new PostalAddress("21 Jump Street","Los Angeles","CA","United States",64000)));
        customerStore.add(new Customer("Mike","Jordan",new PostalAddress("5335 Wisconsin Avenue NW","Washington DC","Washington","United States",20015)));
        customerStore.add(new Customer("Shia","Labeouf",new PostalAddress("3727 W. Magnolia","Burbank","CA","United States",91505)));
    }

    private static void generateOrders() {
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

    private static void generateDeliveries() {
        Random randomUtil = new Random();
        for (final Customer customer : customerStore.getStorage()) {
            final int size = transportationStore.getStorage().size();
            deliveryStore.add(new Delivery(orderStore.getByCustomer(customer),transportationStore.getStorage().get(randomUtil.nextInt(size)),company.getHeadquarters(),company.getHeadquarters().asCoordinates(),customer.getPostalAddress()));
        }
    }

    private static void deleteAll() {
        JedisManager.getInstance().getResource().flushDB();
    }
}
