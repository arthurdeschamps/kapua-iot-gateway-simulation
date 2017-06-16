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

    /**
     * Creates a new order in the order store and take all the products of the order out of the product store.
     * @param order
     * Object of type Order.
     */
    public void newOrder(Order order) {
        // Remove from product store all products from order
        order.getOrderedProducts().forEach(product -> productStore.delete(product));
        orderStore.add(order);
    }

    /**
     * Removes order from the order store of the company and puts back the products in the product store.
     * @param order
     * Order to be cancelled.
     */
    public void cancelOrder(Order order) {
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

    /**
     * Tries to find an available transportation (not assigned to a delivery) and returns it. If there is no available
     * transportation, null is returned.
     * @return
     * Optional object of type Transportation.
     */
    public Optional<Transportation> getAvailableTransportation() {
        for (final Transportation candidate : transportationStore.getStorage()) {
            if (deliveryStore.getStorage().stream().noneMatch(delivery -> delivery.getTransporter().equals(candidate))) {
                return Optional.of(candidate);
            }

        }
        return Optional.empty();
    }

    public void newTransportation(Transportation transportation) {
        transportationStore.add(transportation);
    }

    /**
     * Return false if transportation is assigned to any delivery and true otherwise. If the result is true, the
     * given transportation is deleted from the transportation store of the company.
     */
    public boolean deleteTransportation(Transportation transportation) {
        if (deliveryStore.getStorage().stream().anyMatch(delivery -> delivery.getTransporter().equals(transportation)))
            return false;
        transportationStore.delete(transportation);
        return true;
    }

    /**
     * Creates a new delivery and stores it in the delivery store of the company. The order that shall be delivered is
     * taken off of the order store.
     * @param delivery
     * Object of type Delivery.
     */
    public void newDelivery(Delivery delivery) {
        orderStore.delete(delivery.getOrder());
        deliveryStore.add(delivery);
    }

    /**
     * Cancels a delivery and puts back the order in the order store of the company.
     * @param delivery
     * Delivery to be cancelled.
     */
    public void cancelDelivery(Delivery delivery) {
        orderStore.add(delivery.getOrder());
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
