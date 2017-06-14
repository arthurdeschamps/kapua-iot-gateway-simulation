package company.product;

import simulator.main.Coordinate;
import storage.Item;

/**
 * Created by Arthur Deschamps on 02.06.17.
 * Defines a Product/item of type ProductType
 */
public class Product extends Item {

    private ProductType productType;
    private Coordinate currentLocation;
    private float price;

    public Product(ProductType productType, Coordinate currentLocation, float price) {
        this.currentLocation = currentLocation;
        this.productType = productType;
        this.price = price;
    }

    public Product(ProductType productType, Coordinate currentLocation) {
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

    public Coordinate getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Coordinate currentLocation) {
        this.currentLocation = currentLocation;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
