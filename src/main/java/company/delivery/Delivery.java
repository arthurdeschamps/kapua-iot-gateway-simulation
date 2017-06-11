package company.delivery;

import company.order.Order;
import company.transportation.PostalAddress;
import company.transportation.Transportation;
import jedis.JedisObject;
import redis.clients.jedis.GeoCoordinate;

/**
 * Created by Arthur Deschamps on 02.06.17.
 */
public class Delivery extends JedisObject {

    private Order order;
    private Transportation transporter;
    private PostalAddress destination;
    private PostalAddress departure;
    private GeoCoordinate currentPosition;
    private DeliveryStatus deliveryState;

    public Delivery(Order order, Transportation transporter, PostalAddress departure, GeoCoordinate currentPosition, PostalAddress destination) {
        this.order = order;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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
