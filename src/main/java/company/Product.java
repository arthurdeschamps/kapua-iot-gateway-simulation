package company;

import jedis.JedisManager;
import jedis.JedisObject;

import java.util.UUID;

/**
 * Created by Arthur Deschamps on 30.05.17.
 * This class represents a product sold by the company
 */
public class Product implements JedisObject {

    private String id;
    private String name;
    private String productionCountry;
    private float price; // in USD
    private float weight; // in grams
    private boolean isFragile;


    public Product(String name, String productionCountry, float price, float weight, boolean isFragile) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.productionCountry = productionCountry;
        this.price = price;
        this.weight = weight;
        this.isFragile = isFragile;
    }


    // Validate object before saving
    public boolean validate() {
        return ((name.length() <= 50) && (productionCountry.length() <= 50) && (price > 0) && (weight > 0));
    }

    @Override
    public void save() {
        JedisManager.getInstance().save(this);
    }

    @Override
    public void delete() {
        JedisManager.getInstance().delete(this);
    }


    @Override
    public String getId() {
        return Product.class.getName().toLowerCase()+":"+id+":";
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
        return isFragile;
    }
}
