package simulation.runners;

import company.address.Coordinates;
import company.delivery.Delivery;
import company.company.Company;
import company.delivery.DeliveryStatus;
import company.transportation.Transportation;

import java.util.Random;

/**
 * Simulates product movement during deliveries. The base time unit of this class is 1 hour, that is 3600 seconds.
 * @author Arthur Deschamps
 * @since 1.0
 */
public class DeliveryMovementSimulatorRunner implements Runnable {

    private Company company;

    public DeliveryMovementSimulatorRunner(Company company) {
        this.company = company;
    }

    @Override
    public void run() {
        company.getDeliveries().stream().filter(delivery -> delivery.getDeliveryState().equals(DeliveryStatus.TRANSIT))
                .forEach(this::moveDelivery);
    }

    /**
     * Simulates delivery movement during one second. A delivery moves toward the direction of its destination.
     * @param delivery
     * Delivery to be moved
     */
    private void moveDelivery(Delivery delivery) {
        // Get speed in km/s
        final float speed = getActualSpeed(delivery.getTransporter());

        // We simulate 1 hour
        final float distance = speed*3600;

        // Move in the distance-minimising direction
        delivery.setCurrentLocation(getDistanceMinimizerCoordinates(
                delivery.getCurrentLocation(),delivery.getDestination().getCoordinates(),distance)
        );

        // Check if arrived at destination
        if (atDestination(delivery.getDestination().getCoordinates(),delivery.getCurrentLocation())) {
            confirmDelivery(delivery);
        }
    }

    /**
     * Determines if the current location of a delivery is "close enough" to its destination to mark it as "delivered"
     * @param destination
     * Delivery's destination
     * @param currentLocation
     * Delivery's current location
     * @return
     * True if current location can be considered as destination, false otherwise.
     */
    private boolean atDestination(Coordinates destination, Coordinates currentLocation) {
        // Maximum tolerated distance from destination
        final int toleranceInKm = 30;

        return Coordinates.calculateDistance(destination,currentLocation) < toleranceInKm;
    }

    /**
     * Determines the route to take to minimize the distance between the current location and the destination.
     * @param basePoint
     * Current location of the delivery.
     * @param distance
     * Total distance that the delivery shall move to.
     * @return
     * New coordinates that are as close as possible to the destination considering the traveled destination.
     */
    private Coordinates getDistanceMinimizerCoordinates(Coordinates basePoint, Coordinates destination, final float distance) {

        // Min is the base point at first
        Coordinates minCoordinates = basePoint;
        Coordinates intermediateCoordinates;
        double minDistance = Coordinates.calculateDistance(minCoordinates,destination);
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
                    intermediateCoordinates = Coordinates.applyDistance(basePoint,(float)distanceX,(float)distanceY);
                    intermediateDistance = Coordinates.calculateDistance(intermediateCoordinates,destination);
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

    /**
     * Calculates the actual speed of a transportation. The calculation involves randomness.
     * @param transporter
     * The transporter we need to compute the actual speed of.
     * @return
     * A float representing a speed in km/s
     */
    private float getActualSpeed(Transportation transporter) {
        double speed = (double)transporter.getMaxSpeed()/(double) 3600; // Converts km/h to km/s


        // Includes a randomness factor (speed is not constant during a delivery process) in the form of a bonus or
        // penalty from 1 to 5 %.
        Random random = new Random();
        final int randomFactor = (int) Math.pow(-1,random.nextInt(2))*(random.nextInt(5)+1);

        return (float) (speed+speed*randomFactor/100);
    }

    /**
     * Terminates a delivery (when it got the the destination address).
     * @param delivery
     * The delivery to terminate.
     */
    private void confirmDelivery(Delivery delivery) {
        company.confirmDelivery(delivery);
    }
}
