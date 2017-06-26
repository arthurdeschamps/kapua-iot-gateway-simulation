package kapua.gateway;

import company.company.Company;
import company.delivery.Delivery;
import company.delivery.DeliveryStatus;
import company.transportation.Transportation;
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
    private Payload.Builder payload;

    Sender(Company company, Application application) {
        this.company = company;
        this.application = application;
    }

    /**
     * Sends all telemetry data to Kapua
     */
    void updateData() {
        updateDeliveriesLocations();
        updateTransportationHealthState();
    }

    /**
     * Sends deliveries' locations when they are in transit
     */
    private void updateDeliveriesLocations() {
        payload = new Payload.Builder();

        for (final Delivery delivery : company.getDeliveries())
            if (delivery.getDeliveryState().equals(DeliveryStatus.TRANSIT))
                payload.put(delivery.getId(),delivery.getCurrentLocation().toString());
        send(payload,"Deliveries","Locations");
    }

    /**
     * Sends transportation health states when they are active (attached to a delivery)
     */
    private void updateTransportationHealthState() {
        payload = new Payload.Builder();

        for(final Transportation transportation : company.getAllTransportation())
            if (!transportation.isAvailable())
                payload.put(transportation.getId(),transportation.getHealthState());
        send(payload,"Transportation","Health states");
    }

    /**
     * Actually sends data to Kapua
     * @param payload
     * Payload filled with the data to be sent.
     * @param mainTopic
     * Principal topic of the data.
     * @param subCategories
     * Sub categories (topics).
     */
    private void send(Payload.Builder payload, String mainTopic, String... subCategories) {
        if (payload.values().size() > 0) {
            try {
                application.data(Topic.of(mainTopic,subCategories)).send(payload);
            } catch (Exception e) {
                Logger.getLogger(Sender.class.getName()).warning(e.getMessage());
            }
        }
    }

}
