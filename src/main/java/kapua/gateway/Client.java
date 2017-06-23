package kapua.gateway;

import company.customer.Customer;
import company.main.Company;
import economy.Economy;
import org.eclipse.kapua.gateway.client.Application;
import org.eclipse.kapua.gateway.client.Payload;
import org.eclipse.kapua.gateway.client.Topic;
import org.eclipse.kapua.gateway.client.mqtt.fuse.FuseClient;
import org.eclipse.kapua.gateway.client.profile.kura.KuraMqttProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.eclipse.kapua.gateway.client.Credentials.userAndPassword;
import static org.eclipse.kapua.gateway.client.Transport.waitForConnection;

/**
 * Interface and communicate with Kapua
 * @author Arthur Deschamps
 * @since 1.0
 */
public class Client {

    private Company company;
    private Economy economy;
    private org.eclipse.kapua.gateway.client.Client client;
    private Application application;

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public Client(Economy economy, Company company) {
        this.company = company;
        this.economy = economy;

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
    public void startPublicationsAndSubscriptions() {
        try {
            // Wait for connection
            waitForConnection(application.transport());

            startSubscriptions();
            startPublications();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts all subscriptions.
     * @throws Exception
     */
    private void startSubscriptions() throws Exception {
        application.data(Topic.of("company","customer")).subscribe(this::subscriptionHandler);
    }

    /**
     * Starts all publications.
     * @throws Exception
     */
    private void startPublications() throws Exception {
        final Payload.Builder payload = new Payload.Builder();

        // Customers
        Customer customer = company.getCustomers().iterator().next();
        payload.put(customer.getId(), customer.toJson());

        application.data(Topic.of("company","customer")).send(payload);

    }

    /**
     * Handles message reception
     * @param message
     * Message received from Kapua.
     */
    private void subscriptionHandler(Payload message) {
        logger.info("Received: "+message.toString());
    }

    /**
     * Handles sending failures
     * @param throwable
     * Exception to handle
     * @param message
     * Error messages
     */
    private void publicationHandler(Throwable throwable, Optional<Payload> message) {
        logger.info("Failed to publish: "+throwable.getMessage());
    }


}
