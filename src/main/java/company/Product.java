package company;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Arthur Deschamps on 30.05.17.
 * This class represents a product sold by the company
 */
@Entity
@Table(name="products")
public class Product extends BaseModel {

    @Size(min = 1, max = 100)
    private String name;
    @Size(min = 1, max = 200)
    private String productionCountry;
    @NotNull
    private float price; // in USD
    @NotNull
    private float weight; // in grams
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
