package simulator.runner;

import company.address.Coordinate;
import company.delivery.Delivery;
import company.transportation.Transportation;

import java.util.Random;

/**
 * Simulates product movement during deliveries
 * @author Arthur Deschamps
 * @since 1.0
 */
public class DeliveryMovementSimulatorRunner implements Runnable {

    private CompanySimulatorRunner companySimulatorRunner;

    public DeliveryMovementSimulatorRunner(CompanySimulatorRunner companySimulatorRunner) {
        this.companySimulatorRunner = companySimulatorRunner;
    }

    @Override
    public void run() {
        companySimulatorRunner.getCompany().getDeliveries().forEach(this::moveDelivery);
    }

    /**
     * Simulates delivery movement during one second. A delivery moves toward the direction of its destination.
     * @param delivery
     * Delivery to be moved
     */
    private void moveDelivery(Delivery delivery) {
        // Get speed in km/s
        final float speed = getActualSpeed(delivery.getTransporter());

        // Since we simulate 1 second, speed = distance in km. We can then convert km to degrees
        //TODO decide x-distance and y-distance
        //final Coordinate newCoordinate = Coordinate.applyDistance(delivery.getCurrentLocation(),speed/2,speed/2);

        // Moves in the direction of the longest distance (x or y)
    }

    /**
     * Calculates the actual speed of a transportation. The calculation involves randomness.
     * @param transporter
     * The transporter we need to compute the actual speed of.
     * @return
     * A float representing a speed in km/s
     */
    private float getActualSpeed(Transportation transporter) {
        // Movement quickness depends on the transportation mode and max speed
        float speed = transporter.getMaxSpeed()/3600; // Converts km/h to km/s
        switch (transporter.getTransportationMode()) {
            case AIR:
                speed = speed*95/100;
                break;
            case LAND_RAIL:
                speed = speed*85/100;
                break;
            case LAND_ROAD:
                speed = speed*70/100;
            case WATER:
                speed = speed*65/100;
        }

        // Includes a randomness factor (speed is not constant during a delivery process) in the form of a bonus or
        // penalty from 1 to 5 %.
        Random random = new Random();
        final int randomFactor = (int) Math.pow(-1,random.nextInt(2))*(random.nextInt(5)+1);

        return speed+speed*randomFactor/100;
    }

    /**
     * Terminates a delivery (when it got the the destination address).
     * @param delivery
     * The delivery to terminate.
     */
    private void confirmDelivery(Delivery delivery) {

    }
}
