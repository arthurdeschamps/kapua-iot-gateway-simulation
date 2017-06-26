package simulation.simulators.company;

import company.company.Company;
import company.delivery.Delivery;
import company.delivery.DeliveryStatus;
import company.order.Order;
import economy.Economy;
import simulation.util.ProbabilityUtils;

import java.util.Iterator;

/**
 * Simulates everything related to deliveries (except telemetry data)
 * @since 1.0
 * @author Arthur Deschamps
 */
public class DeliverySimulator extends AbstractCompanyComponentSimulator {

    public DeliverySimulator(Company company, Economy economy) {
        super(company, economy);
    }

    @Override
    public void run() {
        simulateNewDeliveries();
        simulateDeliveriesShipping();
    }

    /**
     * Simulates new deliveries.
     * @since 1.0
     */
    private void simulateNewDeliveries() {
        Iterator<Order> iterator = company.getOrders().iterator();
        while (iterator.hasNext()) {
            if (probabilityUtils.event(1, ProbabilityUtils.TimeUnit.HOUR)) {
                final Order order = iterator.next();
                company.getAvailableTransportation().ifPresent(transportation -> company.getDeliveryStore().add(
                        new Delivery(order,transportation,company.getHeadquarters(),order.getBuyer().getAddress())
                ));
                iterator.remove();
            }
        }
    }

    /**
     * Simulates deliveries shipping. A delivery that is still at the warehouse will be shipped eventually.
     */
    private void simulateDeliveriesShipping() {
        company.getDeliveries()
                .stream()
                .filter(delivery -> delivery.getDeliveryState().equals(DeliveryStatus.WAREHOUSE))
                .forEach(delivery -> {
                    if (probabilityUtils.event(1, ProbabilityUtils.TimeUnit.DAY)) {
                        delivery.setDeliveryState(DeliveryStatus.TRANSIT);
                    }
                });
    }
}
