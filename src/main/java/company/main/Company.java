package company.main;

import company.customer.PostalAddress;
import company.order.DeliveryStore;
import company.product.ProductStore;
import company.product.ProductTypeStore;
import company.transportation.TransportationStore;
import redis.clients.jedis.GeoCoordinate;

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

    public Company(CompanyType companyType, String name, PostalAddress postalAddress) {
        this.productStore = new ProductStore();
        this.productTypeStore = new ProductTypeStore();
        this.deliveryStore = new DeliveryStore();
        this.transportationStore = new TransportationStore();
        this.type = companyType;
        this.name = name;
        this.headquarters = postalAddress;
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

    public ProductStore getProductStore() {
        return productStore;
    }

    public void setProductStore(ProductStore productStore) {
        this.productStore = productStore;
    }

    public ProductTypeStore getProductTypeStore() {
        return productTypeStore;
    }

    public void setProductTypeStore(ProductTypeStore productTypeStore) {
        this.productTypeStore = productTypeStore;
    }

    public DeliveryStore getDeliveryStore() {
        return deliveryStore;
    }

    public void setDeliveryStore(DeliveryStore deliveryStore) {
        this.deliveryStore = deliveryStore;
    }

    public TransportationStore getTransportationStore() {
        return transportationStore;
    }

    public void setTransportationStore(TransportationStore transportationStore) {
        this.transportationStore = transportationStore;
    }
}
