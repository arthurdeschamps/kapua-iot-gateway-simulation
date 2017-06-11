package company.main;

import company.customer.Customer;
import company.customer.CustomerStore;
import company.transportation.PostalAddress;
import company.delivery.Delivery;
import company.delivery.DeliveryStore;
import company.order.Order;
import company.order.OrderStore;
import company.product.Product;
import company.product.ProductStore;
import company.product.ProductType;
import company.product.ProductTypeStore;
import company.transportation.Transportation;
import company.transportation.TransportationStore;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by Arthur Deschamps on 30.05.17.
 */
public class Company {

    private CompanyType type;
    private String name;
    private PostalAddress headquarters;

    // Object stores
    private ProductStore productStore;
    private ProductTypeStore productTypeStore;
    private DeliveryStore deliveryStore;
    private TransportationStore transportationStore;
    private CustomerStore customerStore;
    private OrderStore orderStore;

    public Company(CompanyType companyType, String name, PostalAddress postalAddress) {
        this.productStore = new ProductStore();
        this.productTypeStore = new ProductTypeStore();
        this.deliveryStore = new DeliveryStore();
        this.transportationStore = new TransportationStore();
        this.customerStore = new CustomerStore();
        this.orderStore = new OrderStore();
        this.type = companyType;
        this.name = name;
        this.headquarters = postalAddress;
    }

    public void newCustomer(Customer customer) {
        customerStore.add(customer);
    }

    // Return false if customer currently has orders
    public boolean deleteCustomer(Customer customer) {
        if (!hasOrders(customer)) {
            customerStore.delete(customer);
            return true;
        }
        return false;
    }

    public boolean deleteRandomCustomer() {
        if (customerStore.getStorage().size() > 0) {
            Random random = new Random();
            Customer randomCustomer = customerStore.getStorage().get(random.nextInt(customerStore.getStorage().size()));
            if (!hasOrders(randomCustomer)) {
                customerStore.delete(randomCustomer);
                return true;
            }
        }
        return false;
    }

    private boolean hasOrders(Customer customer) {
        return (customer != null) && (orderStore.getStorage().size() > 0) && orderStore.getStorage().stream().anyMatch(order -> order.getBuyer().equals(customer));
    }

    public List<Customer> getCustomers() {
        return customerStore.getStorage();
    }

    public void newProduct(Product product) {
        productStore.add(product);
    }

    public void deleteProduct(Product product) {
        productStore.delete(product);
    }

    public void newProductType(ProductType productType) {
        productTypeStore.add(productType);
    }

    public void deleteProductType(ProductType productType) {
        // Delete all products of same type
        productStore.getStorage().stream().filter(product -> product.getProductType().equals(productType))
                .forEach(product -> productStore.delete(product));
        productTypeStore.delete(productType);
    }

    public List<Product> getProducts() {
        return productStore.getStorage();
    }

    public List<ProductType> getProductTypes() {
        return productTypeStore.getStorage();
    }

    public long getProductQuantity(ProductType productType) {
        return productStore.getProductTypeQuantity(productType);
    }

    public void newOrder(Order order) {
        // Remove from product store all products from order
        order.getOrderedProducts().forEach(product -> productStore.delete(product));
        orderStore.add(order);
    }

    public void deleteOrder(Order order) {
        // Puts back products in product store
        order.getOrderedProducts().forEach(product -> productStore.add(product));
        orderStore.delete(order);
    }

    public List<Order> getOrdersForProductType(ProductType productType) {
        return orderStore.getStorage().stream().filter(order ->
                order.getOrderedProducts().stream().anyMatch(product -> product.getProductType().equals(productType)))
                .collect(Collectors.toList());
    }

    public List<Order> getOrders() {
        return orderStore.getStorage();
    }

    public List<Transportation> getAllTransportation() {
        return transportationStore.getStorage();
    }

    public Optional<Transportation> getAvailableTransportation() {
        for (final Transportation candidate : transportationStore.getStorage()) {
            if (deliveryStore.getStorage().stream().noneMatch(delivery -> delivery.getTransporter().equals(candidate))) {
                return Optional.of(candidate);
            }

        }
        return null;
    }

    public void newTransportation(Transportation transportation) {
        transportationStore.add(transportation);
    }

    // Return false if transportation is assigned to deliveries
    public boolean deleteTransportation(Transportation transportation) {
        if (deliveryStore.getStorage().stream().anyMatch(delivery -> delivery.getTransporter().equals(transportation)))
            return false;
        transportationStore.delete(transportation);
        return true;
    }

    public void newDelivery(Delivery delivery) {
        deliveryStore.add(delivery);
    }

    public void deleteDelivery(Delivery delivery) {
        deliveryStore.delete(delivery);
    }

    public CompanyType getType() {
        return type;
    }

    public void setType(CompanyType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PostalAddress getHeadquarters() {
        return headquarters;
    }

    public void setHeadquarters(PostalAddress headquarters) {
        this.headquarters = headquarters;
    }
}
