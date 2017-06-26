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

    /**
     * Determines if the current location of a delivery is "close enough" to its destination to mark it as "delivered"
     * @return
     * True if current location can be considered as destination, false otherwise.
     */
    public boolean isAtDestination() {
        // Maximum tolerated distance from destination
        final int toleranceInKm = 30;
        return Coordinates.calculateDistance(this.getDestination().getCoordinates(),this.getCurrentLocation()) < toleranceInKm;
    }

    /**
     * Determines the route to take to minimize the distance between the current location and the destination.
     * @param distance
     * Total distance that the delivery shall move to.
     * @return
     * New coordinates that are as close as possible to the destination considering the traveled destination.
     */
    public Coordinates minimizeDistanceFromDestination(final float distance) {

        // Min is the base point at first
        Coordinates minCoordinates = this.getCurrentLocation();
        Coordinates intermediateCoordinates;
        double minDistance = Coordinates.calculateDistance(minCoordinates,this.getDestination().getCoordinates());
        double distanceX = -distance;
        double distanceY, intermediateDistance;
        // Step size
        final double eps = 1;

        // Evaluation with each possible x and y distances
        while (distanceX <= distance) {
            distanceY = -distance;
            while (distanceY <= distance) {
                // Constraint of our optimization problem
                if (Math.abs(distanceX) + Math.abs(distanceY) <= distance) {
                    intermediateCoordinates = Coordinates.applyDistance(this.getCurrentLocation(),(float)distanceX,(float)distanceY);
                    intermediateDistance = Coordinates.calculateDistance(intermediateCoordinates,this.getDestination().getCoordinates());
                    if (minDistance >= intermediateDistance) {
                        minCoordinates = intermediateCoordinates;
                        minDistance = intermediateDistance;
                    }
                }

                distanceY += eps;
            }
            distanceX += eps;
        }

        return minCoordinates;

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
