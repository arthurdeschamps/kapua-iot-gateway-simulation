package kapua.gateway;

import company.company.Company;
import company.delivery.Delivery;
import company.delivery.DeliveryStatus;
import org.eclipse.kapua.gateway.client.Application;
import org.eclipse.kapua.gateway.client.Payload;
import org.eclipse.kapua.gateway.client.Topic;
import org.eclipse.kapua.gateway.client.mqtt.fuse.FuseClient;
import org.eclipse.kapua.gateway.client.profile.kura.KuraMqttProfile;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static org.eclipse.kapua.gateway.client.Credentials.userAndPassword;
import static org.eclipse.kapua.gateway.client.Transport.waitForConnection;

/**
 * Interface and communicate with Kapua
 * @author Arthur Deschamps
 * @since 1.0
 */
public class KapuaGatewayClient {

    private Company company;
    private org.eclipse.kapua.gateway.client.Client client;
    private Application application;
    private int communicationsDelay;

    private static final Logger logger = Logger.getLogger(KapuaGatewayClient.class.getName());

    public KapuaGatewayClient(Company company, int communicationsDelay) {
        this.company = company;
        this.communicationsDelay = communicationsDelay;

        try {
            client = KuraMqttProfile.newProfile(FuseClient.Builder::new)
                    .accountName("kapua-sys")
                    .clientId("supply-chain-control-simulator")
                    .brokerUrl("tcp://localhost:1883")
                    .credentials(userAndPassword("kapua-broker", "kapua-password"))
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }

        application = client.buildApplication("Supply Chain Control Simulator").build();

    }

    /**
     * Initialize all publications and subscriptions. Every data created in the simulation will be transferred to Kapua.
     */
    public void startCommunications() {
        try {
            // Wait for connection
            waitForConnection(application.transport());

            startSubscriptions();

            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
            executorService.scheduleWithFixedDelay(this::updateDeliveries,0,
                    communicationsDelay, TimeUnit.SECONDS);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts all subscriptions.
     * @throws Exception
     */
    private void startSubscriptions() throws Exception {
        application.data(Topic.of("company","deliveries","locations")).subscribe(this::subscriptionHandler);
    }

    /**
     * Sends deliveries' locations
     */
    private void updateDeliveries() {
        final Payload.Builder payload = new Payload.Builder();

        for (final Delivery delivery : company.getDeliveries())
            if (delivery.getDeliveryState().equals(DeliveryStatus.TRANSIT))
                payload.put(delivery.getId(),delivery.getCurrentLocation().toString());

        // Sends everything
        try {
            application.data(Topic.of("company","deliveries","locations")).send(payload);
        } catch (Exception e) {
            logger.warning(e.getMessage());
        }
    }

    /**
     * Handles message reception
     * @param message
     * Message received from Kapua.
     */
    private void subscriptionHandler(Payload message) {
        logger.info("Received: "+message.toString());
    }



}
