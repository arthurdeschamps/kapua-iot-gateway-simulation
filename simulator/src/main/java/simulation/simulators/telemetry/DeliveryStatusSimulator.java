package simulation.simulators.telemetry;

import company.company.Company;
import company.delivery.Delivery;
import company.delivery.DeliveryStatus;
import simulation.util.ProbabilityUtils;

/**
 * Simulates everything related to deliveries' status
 * @since 1.0
 * @author Arthur Deschamps
 */
public class DeliveryStatusSimulator extends AbstractTelemetryComponentSimulator {

    public DeliveryStatusSimulator(Company company) {
        super(company);
    }

    @Override
    public void run() {
        company.getDeliveries().forEach(delivery -> {
            if (delivery.getDeliveryState().equals(DeliveryStatus.TRANSIT))
                checkDeliveriesAtDestination(delivery);
            if (delivery.getDeliveryState().equals(DeliveryStatus.WAREHOUSE)) {
                simulateDeliveriesShipping(delivery);
                simulateDeliveryCancellation(delivery);
            }
        });
    }

    /**
     * Simulates delivery shipping. A delivery will be shipped eventually.
     * @param delivery
     * A delivery that still has WAREHOUSE status.
     */
    private void simulateDeliveriesShipping(Delivery delivery) {
        if (probabilityUtils.event(1, ProbabilityUtils.TimeUnit.DAY))
            company.startDeliveryShipping(delivery);
    }

    /**
     * Checks if the delivery is at destination.
     * @param delivery
     * A delivery that still is in TRANSIT.
     */
    private void checkDeliveriesAtDestination(Delivery delivery) {
        if (delivery.isAtDestination())
            company.confirmDelivery(delivery);
    }

    private void simulateDeliveryCancellation(Delivery delivery) {
        if (probabilityUtils.event(1,ProbabilityUtils.TimeUnit.MONTH))
            company.cancelDelivery(delivery);
    }
}
