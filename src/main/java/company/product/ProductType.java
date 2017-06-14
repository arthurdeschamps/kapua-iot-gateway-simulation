package company.product;

import jedis.JedisObject;


/**
 * Created by Arthur Deschamps on 30.05.17.
 * This class represents a type of product sold by the company.
 * It is the meta-class of the class Product
 */
public class ProductType extends JedisObject {

    private String name;
    private String productionCountry;
    private float basePrice; // in USD
    private float weight; // in grams
    private boolean fragile;


    public ProductType(String name, String productionCountry, float price, float weight, boolean fragile) {
        this.name = name;
        this.productionCountry = productionCountry;
        this.basePrice = price;
        this.weight = weight;
        this.fragile = fragile;
    }


    // Validate object before saving
    public boolean validate() {
        return ((basePrice > 0) && (weight > 0));
    }

    @Override
    public boolean equals(Object obj) {
        try {
            ProductType productType = (ProductType) obj;
            return (productType.getName().equals(this.getName()) && productType.getBasePrice() == this.getBasePrice()
            && productType.getProductionCountry().equals(this.getProductionCountry()) && productType.getWeight() == this.getWeight());
        } catch (ClassCastException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(float basePrice) {
        this.basePrice = basePrice;
    }

    public String getProductionCountry() {
        return productionCountry;
    }

    public float getWeight() {
        return weight;
    }

    public boolean isFragile() {
        return fragile;
    }

    public void setProductionCountry(String productionCountry) {
        this.productionCountry = productionCountry;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setFragile(boolean fragile) {
        this.fragile = fragile;
    }

}
