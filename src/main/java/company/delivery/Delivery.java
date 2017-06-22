package company.delivery;

import company.address.Address;
import company.address.Coordinates;
import company.order.Order;
import company.transportation.Transportation;
import storage.Item;

/**
 * Describes a Delivery of the company.
 * @author Arthur Deschamps
 * @since 1.0
 */
public class Delivery extends Item {

    private Order order;
    private Transportation transporter;
    private Address destination;
    private Address departure;
    private Coordinates currentLocation;
    private DeliveryStatus deliveryState;

    public Delivery(Order order, Transportation transporter, Address departure, Address destination) {
        this.order = order;
        this.transporter = transporter;
        this.departure = departure;
        this.currentLocation = departure.getCoordinates();
        this.departure = departure;
        this.destination = destination;
        this.deliveryState = DeliveryStatus.WAREHOUSE;
    }

    @Override
    public boolean validate() {
        return ((transporter != null) && (destination != null) &&
                (departure != null) && (currentLocation != null) && (deliveryState != null));
    }

    public double getDistanceFromDestination() {
        return Coordinates.calculateDistance(this.getCurrentLocation(),this.getDestination().getCoordinates());
    }

    public boolean isDelivered() {
        return deliveryState.equals(DeliveryStatus.DELIVERED);
    }

    public boolean isInTransit() {
        return !getCurrentLocation().equals(getDeparture().getCoordinates());
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

    public Address getDestination() {
        return destination;
    }

    public void setDestination(Address destination) {
        this.destination = destination;
    }

    public Address getDeparture() {
        return departure;
    }

    public void setDeparture(Address departure) {
        this.departure = departure;
    }

    public Coordinates getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Coordinates currentLocation) {
        this.currentLocation = currentLocation;
    }

    public DeliveryStatus getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(DeliveryStatus deliveryState) {
        this.deliveryState = deliveryState;
    }
}
