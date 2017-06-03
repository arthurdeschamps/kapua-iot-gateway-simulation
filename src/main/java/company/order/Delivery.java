package company.order;

import company.customer.PostalAddress;
import company.product.Product;
import company.transportation.Transportation;
import jedis.JedisManager;
import jedis.JedisObject;
import redis.clients.jedis.GeoCoordinate;

import java.util.List;

/**
 * Created by Arthur Deschamps on 02.06.17.
 */
public class Delivery extends JedisObject {

    private List<Order> orders;
    private Transportation transporter;
    private PostalAddress destination;
    private PostalAddress departure;
    private GeoCoordinate currentPosition;
    private DeliveryStatus deliveryState;

    public Delivery(List<Order> orders, Transportation transporter, PostalAddress departure, GeoCoordinate currentPosition, PostalAddress destination) {
        this.orders = orders;
        this.transporter = transporter;
        this.departure = departure;
        this.currentPosition = currentPosition;
        this.departure = departure;
        this.destination = destination;
        this.deliveryState = DeliveryStatus.WAREHOUSE;
    }

    @Override
    public boolean validate() {
        return ((transporter != null) && (destination != null) &&
                (departure != null) && (currentPosition != null) && (deliveryState != null));
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Transportation getTransporter() {
        return transporter;
    }

    public void setTransporter(Transportation transporter) {
        this.transporter = transporter;
    }

    public PostalAddress getDestination() {
        return destination;
    }

    public void setDestination(PostalAddress destination) {
        this.destination = destination;
    }

    public PostalAddress getDeparture() {
        return departure;
    }

    public void setDeparture(PostalAddress departure) {
        this.departure = departure;
    }

    public GeoCoordinate getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(GeoCoordinate currentPosition) {
        this.currentPosition = currentPosition;
    }

    public DeliveryStatus getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(DeliveryStatus deliveryState) {
        this.deliveryState = deliveryState;
    }
}
