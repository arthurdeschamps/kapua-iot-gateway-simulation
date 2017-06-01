package company;

import jedis.JedisObject;


/**
 * Created by Arthur Deschamps on 30.05.17.
 * This class represents a product sold by the company
 */
public class Product extends JedisObject {

    // Id
    private String name;
    private String productionCountry;
    private float price; // in USD
    private float weight; // in grams
    private boolean fragile;


    public Product(String name, String productionCountry, float price, float weight, boolean fragile) {
        this.name = name;
        this.productionCountry = productionCountry;
        this.price = price;
        this.weight = weight;
        this.fragile = fragile;
    }


    // Validate object before saving
    public boolean validate() {
        return ((name.length() <= 50) && (productionCountry.length() <= 50) && (price > 0) && (weight > 0));
    }


    @Override
    public String getId() {
        return Product.class.getName().toLowerCase()+":"+name.toLowerCase().replace(' ','_')+":";
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
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
