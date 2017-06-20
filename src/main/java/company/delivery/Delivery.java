package company.delivery;

import company.order.Order;
import company.transportation.PostalAddress;
import company.transportation.Transportation;
import simulator.main.Coordinate;
import storage.Item;

/**
 * Created by Arthur Deschamps on 02.06.17.
 */
public class Delivery extends Item {

    private Order order;
    private Transportation transporter;
    private PostalAddress destination;
    private PostalAddress departure;
    private Coordinate currentLocation;
    private DeliveryStatus deliveryState;

    public Delivery(Order order, Transportation transporter, PostalAddress departure, PostalAddress destination) {
        this.order = order;
        this.transporter = transporter;
        this.departure = departure;
        this.currentLocation = departure.toCoordinates();
        this.departure = departure;
        this.destination = destination;
        this.deliveryState = DeliveryStatus.WAREHOUSE;
    }

    @Override
    public boolean validate() {
        return ((transporter != null) && (destination != null) &&
                (departure != null) && (currentLocation != null) && (deliveryState != null));
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

    public Coordinate getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Coordinate currentLocation) {
        this.currentLocation = currentLocation;
    }

    public DeliveryStatus getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(DeliveryStatus deliveryState) {
        this.deliveryState = deliveryState;
    }
}
