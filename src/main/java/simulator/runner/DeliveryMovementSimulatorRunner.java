package simulator.runner;

import company.delivery.Delivery;

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

    }
}
