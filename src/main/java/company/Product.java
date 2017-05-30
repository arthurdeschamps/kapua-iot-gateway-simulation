package company;

/**
 * Created by Arthur Deschamps on 30.05.17.
 * This class represents a product sold by the company
 */
public class Product {
    
    private String Id;
    private String name;
    private String productionCountry;
    private float price;
    private float weight;
    private boolean isFragile;


    public Product(String name, String productionCountry, float price, float weight, boolean isFragile) {
        this.name = name;
        this.productionCountry = productionCountry;
        this.price = price;
        this.weight = weight;
        this.isFragile = isFragile;
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

    public String getId() {
        return Id;
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
