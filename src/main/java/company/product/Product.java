package company.product;

import jedis.JedisManager;
import jedis.JedisObject;
import redis.clients.jedis.GeoCoordinate;

/**
 * Created by Arthur Deschamps on 02.06.17.
 * Defines a Product/item of type ProductType
 */
public class Product extends JedisObject {

    private ProductType productType;
    private GeoCoordinate currentLocation;
    private float price;

    public Product(ProductType productType, GeoCoordinate currentLocation, float price) {
        this.currentLocation = currentLocation;
        this.productType = productType;
        this.price = price;
    }

    public Product(ProductType productType, GeoCoordinate currentLocation) {
        this.id = JedisManager.getInstance().generateUniqueId();
        this.currentLocation = currentLocation;
        this.productType = productType;
        this.price = productType.getBasePrice();
    }

    @Override
    public boolean validate() {
        return ((productType != null) && (currentLocation != null));
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public GeoCoordinate getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(GeoCoordinate currentLocation) {
        this.currentLocation = currentLocation;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
