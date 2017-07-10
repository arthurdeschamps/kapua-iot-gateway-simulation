package communications.kapua.gateway;

import company.company.Company;
import company.delivery.Delivery;
import company.delivery.DeliveryStatus;
import company.transportation.Transportation;
import org.eclipse.kapua.gateway.client.Application;
import org.eclipse.kapua.gateway.client.Payload;
import org.eclipse.kapua.gateway.client.Topic;
import org.slf4j.LoggerFactory;

/**
 * Sends telemetry data to Kapua.
 * @since 1.0
 * @author Arthur Deschamps
 */
public class DataSenderRunner implements Runnable {

    private Company company;
    private Application application;
    private Payload.Builder payload;

    DataSenderRunner(Company company, Application application) {
        this.company = company;
        this.application = application;
    }

    /**
     * Sends all telemetry data to Kapua
     */
    @Override
    public void run() {
        company.getDeliveries()
                .forEach(delivery -> {
                    updateDeliveryStatus(delivery);
                    if (delivery.getDeliveryState().equals(DeliveryStatus.TRANSIT)) {
                        updateDeliveryLocation(delivery);
                        updateTransportationHealthState(delivery.getTransporter());
                    }
                });
    }

    /**
     * Sends delivery location
     * @param delivery
     * A delivery that is in transit
     */
    private void updateDeliveryLocation(final Delivery delivery) {
        payload = new Payload.Builder();

        // Sends latitude
        payload.put("latitude",Float.toString(delivery.getCurrentLocation().getLatitude()));
        send(payload,"deliveries","locations","coordinates",delivery.getId(),"latitudes");

        payload = new Payload.Builder();

        // Sends longitude
        payload.put("longitude",Float.toString(delivery.getCurrentLocation().getLongitude()));
        send(payload,"deliveries","locations","coordinates",delivery.getId(),"longitudes");
    }

    /**
     * Sends transportation health state
     * @param transportation
     * A transportation that is attached to a delivery in transit
     */
    private void updateTransportationHealthState(final Transportation transportation) {
        payload = new Payload.Builder();
        payload.put("health",transportation.getHealthState().name());
        send(payload, "transports","health-state", transportation.getId());
    }

    /**
     * Updates all deliveries status.
     * @param delivery
     * Any delivery.
     */
    private void updateDeliveryStatus(final Delivery delivery) {
        payload = new Payload.Builder();
        payload.put("status",delivery.getDeliveryState().name());
        send(payload,"deliveries","status",delivery.getId());
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
                LoggerFactory.getLogger(DataSenderRunner.class).info(e.getMessage());
            }
        }
    }

}
