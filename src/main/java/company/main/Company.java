package company.main;

import company.customer.Customer;
import company.transportation.PostalAddress;
import company.delivery.Delivery;
import company.order.Order;
import company.product.Product;
import company.product.ProductType;
import company.transportation.Transportation;
import storage.ItemStore;

import java.util.*;
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
    private ItemStore<Product> productStore;
    private ItemStore<ProductType> productTypeStore;
    private ItemStore<Delivery> deliveryStore;
    private ItemStore<Transportation> transportationStore;
    private ItemStore<Customer> customerStore;
    private ItemStore<Order> orderStore;

    public Company(CompanyType companyType, String name, PostalAddress postalAddress) {
        this.productStore = new ItemStore<>();
        this.productTypeStore = new ItemStore<>();
        this.deliveryStore = new ItemStore<>();
        this.transportationStore = new ItemStore<>();
        this.customerStore = new ItemStore<>();
        this.orderStore = new ItemStore<>();
        this.type = companyType;
        this.name = name;
        this.headquarters = postalAddress;
    }

    public void newCustomer(Customer customer) {
        customerStore.add(customer);
    }

    // Return false if customer currently has orders
    public void deleteCustomer(Customer customer) {
        if (!hasOrders(customer))
            customerStore.delete(customer);
    }

    public void deleteRandomCustomer() {
        if (customerStore.getStorage().size() > 0) {
            Random random = new Random();
            Customer randomCustomer = customerStore.getRandom();
            if (!hasOrders(randomCustomer)) {
                customerStore.delete(randomCustomer);
            }
        }
    }

    private boolean hasOrders(Customer customer) {
        return (customer != null) && (orderStore.getStorage().size() > 0) && orderStore.getStorage().stream().anyMatch(order ->
                order.getBuyer().equals(customer));
    }

    public Set<Customer> getCustomers() {
        return customerStore.getStorage();
    }

    public void newProduct(Product product) {
        if (product != null)
            productStore.add(product);
    }

    public void deleteProduct(Product product) {
        productStore.delete(product);
    }

    public void newProductType(ProductType productType) {
        if (productType != null)
            productTypeStore.add(productType);
    }

    public void deleteProductType(ProductType productType) {
        // Delete all products of same type
        List<Product> products = productStore.getStorage().stream().filter(product -> {
            if (product.getProductType() == null)
                Logger.getGlobal().info("product type null for product..");
            return product.getProductType().equals(productType);
        }).collect(Collectors.toList());
        for (final Product product: products)
            this.deleteProduct(product);
        // If there is no more product with the type we want to delete
        if (this.getProducts().stream().noneMatch(product -> product.getProductType().equals(productType)))
            productTypeStore.delete(productType);
    }

    public Set<Product> getProducts() {
        return productStore.getStorage();
    }

    public Set<ProductType> getProductTypes() {
        return productTypeStore.getStorage();
    }

    public long getProductQuantity(ProductType productType) {
        return productStore.getStorage().stream().filter(product -> product.getProductType().equals(productType))
                .collect(Collectors.toList()).size();
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

    public List<Order> getOrdersFromProductType(ProductType productType) {
        return orderStore.getStorage().stream().filter(order ->
                order.getOrderedProducts().stream().anyMatch(product -> product.getProductType().equals(productType)))
                .collect(Collectors.toList());
    }

    public Set<Order> getOrders() {
        return orderStore.getStorage();
    }

    public Set<Delivery> getDeliveries() {
        return deliveryStore.getStorage();
    }

    public Set<Transportation> getAllTransportation() {
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

    public ItemStore<Product> getProductStore() {
        return productStore;
    }

    public ItemStore<ProductType> getProductTypeStore() {
        return productTypeStore;
    }

    public ItemStore<Delivery> getDeliveryStore() {
        return deliveryStore;
    }

    public ItemStore<Transportation> getTransportationStore() {
        return transportationStore;
    }

    public ItemStore<Customer> getCustomerStore() {
        return customerStore;
    }

    public ItemStore<Order> getOrderStore() {
        return orderStore;
    }
}
