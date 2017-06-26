package kapua.gateway;

import company.company.Company;
import company.delivery.Delivery;
import company.delivery.DeliveryStatus;
import org.eclipse.kapua.gateway.client.Application;
import org.eclipse.kapua.gateway.client.Payload;
import org.eclipse.kapua.gateway.client.Topic;

import java.util.logging.Logger;

/**
 * Sends telemetry data to Kapua.
 * @since 1.0
 * @author Arthur Deschamps
 */
class Sender {

    private Company company;
    private Application application;

    Sender(Company company, Application application) {
        this.company = company;
        this.application = application;
    }

    /**
     * Sends all telemetry data to Kapua
     */
    void updateData() {
        updateDeliveriesLocations();
    }

    /**
     * Sends deliveries' locations
     */
    private void updateDeliveriesLocations() {
        final Payload.Builder payload = new Payload.Builder();

        for (final Delivery delivery : company.getDeliveries())
            if (delivery.getDeliveryState().equals(DeliveryStatus.TRANSIT))
                payload.put(delivery.getId(),delivery.getCurrentLocation().toString());

        // Sends everything
        if (payload.values().size() > 0) {
            try {
                application.data(Topic.of("company","deliveries","locations")).send(payload);
            } catch (Exception e) {
                Logger.getLogger(Sender.class.getName()).warning(e.getMessage());
            }
        }
    }

}
