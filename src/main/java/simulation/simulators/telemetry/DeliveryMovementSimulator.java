package simulation.simulators.telemetry;

import company.company.Company;
import company.delivery.Delivery;
import company.delivery.DeliveryStatus;

/**
 * Simulates product movement during deliveries.
 * @since 1.0
 * @author Arthur Deschamps
 */
public class DeliveryMovementSimulator extends AbstractTelemetryComponentSimulator  {

    public DeliveryMovementSimulator(Company company) {
        super(company);
    }

    @Override
    public void run() {
        company.getDeliveries().forEach(delivery -> {
            if (delivery.getDeliveryState().equals(DeliveryStatus.TRANSIT))
                moveDelivery(delivery);
        });
//        company.getDeliveries()
//                .stream()
//                .filter(delivery -> delivery.getDeliveryState().equals(DeliveryStatus.TRANSIT))
//                .forEach(this::moveDelivery);
    }

    /**
     * Simulates delivery movement during one hour. A delivery moves toward the direction of its destination.
     * @param delivery
     * Delivery to be moved.
     */
    private void moveDelivery(Delivery delivery) {
        // Get speed in km/s
        final float speed = delivery.getTransporter().getActualSpeed();

        // We simulate 1 hour
        final float distance = speed*3600;

        // Move in the distance from destination minimising direction
        delivery.setCurrentLocation(delivery.minimizeDistanceFromDestination(distance));

        // Check if arrived at destination
        if (delivery.isAtDestination())
            company.confirmDelivery(delivery);
    }

}
