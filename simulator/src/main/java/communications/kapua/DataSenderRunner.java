package communications.kapua;

import company.company.Company;
import company.delivery.Delivery;
import company.delivery.DeliveryStatus;
import company.transportation.Transportation;
import org.eclipse.kapua.gateway.client.Application;
import org.eclipse.kapua.gateway.client.Payload;
import org.eclipse.kapua.gateway.client.Topic;
import org.slf4j.LoggerFactory;
import storage.ItemStore;

import java.util.HashMap;
import java.util.Map;

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
        Delivery[] deliveries = company.getDeliveries()
                .toArray(new Delivery[company.getDeliveries().size()]);
        for (final Delivery delivery : deliveries) {
            updateDeliveryStatus(delivery);

            if (delivery.getDeliveryState().equals(DeliveryStatus.TRANSIT)) {
                updateDeliveryAssignedTransportation(delivery);
                updateDeliveryLocation(delivery);
                updateTransportationHealthState(delivery.getTransporter());
            }
        }

        updateStoresSizes();
    }

    /**
     * Sends transportation id for each delivery in transit
     * @param delivery
     * A delivery that is has an assigned transportation (that is in transit)
     */
    private void updateDeliveryAssignedTransportation(Delivery delivery) {
        payload = new Payload.Builder();
        payload.put("transportation-id",delivery.getTransporter().getId());
        send(payload, "deliveries","assigned-transportation",delivery.getId());
    }

    /**
     * Sends delivery location
     * @param delivery
     * A delivery that is in transit
     */
    private void updateDeliveryLocation(final Delivery delivery) {
        payload = new Payload.Builder();
        payload.put("coordinates",delivery.getCurrentLocation().toString());
        send(payload,"deliveries","locations","coordinates",delivery.getId());
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
     * Sends the size of each company store.
     */
    private void updateStoresSizes() {
        Map<String, Integer> stores = new HashMap<>();
        stores.put("customers", company.getCustomers().size());
        stores.put("orders", company.getOrders().size());
        stores.put("deliveries", company.getDeliveries().size());
        stores.put("products", company.getProducts().size());
        stores.put("productTypes", company.getProductTypes().size());
        stores.put("transportation", company.getAllTransportation().size());

        stores.forEach((name, quantity) -> {
            payload = new Payload.Builder();
            payload.put("number", quantity);
            send(payload, "company", name, "number");
        });
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
               // LoggerFactory.getLogger(DataSenderRunner.class).info(e.getMessage());
               // e.printStackTrace();
            }
        }
    }
}
