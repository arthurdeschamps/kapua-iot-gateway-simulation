package company.order;

import company.customer.Customer;
import company.product.Product;
import org.slf4j.LoggerFactory;
import storage.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arthur Deschamps on 03.06.17.
 */
public class Order extends Item {

    private List<Product> orderedProducts;
    private Customer buyer;
    private boolean issued;

    public Order(Customer buyer) {
        this.orderedProducts = new ArrayList<>();
        this.buyer = buyer;
        this.issued = false;
    }

    public Order(Customer buyer, List<Product> orderedProducts) {
        this.orderedProducts = orderedProducts;
        this.buyer = buyer;
        this.issued = false;
    }

    @Override
    public boolean validate() {
        if (buyer == null) {
            LoggerFactory.getLogger(Order.class).error("null buyer on order");
        }
        return buyer != null && orderedProducts.size() > 0;
    }

    public List<Product> getOrderedProducts() {
        return orderedProducts;
    }

    public void setOrderedProducts(List<Product> orderedProducts) {
        this.orderedProducts = orderedProducts;
    }


    public int getTotalAmount() {
        int totalAmount = 0;
        for (final Product product : this.getOrderedProducts())
            totalAmount += product.getPrice();
        return totalAmount;
    }

    public boolean isIssued() {
        return issued;
    }

    public void setIssued(boolean issued) {
        this.issued = issued;
    }

    public Customer getBuyer() {
        return buyer;
    }

    public void setBuyer(Customer buyer) {
        this.buyer = buyer;
    }
}
