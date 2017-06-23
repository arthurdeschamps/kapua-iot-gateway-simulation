package kapua.gateway;

import company.main.Company;
import economy.Economy;
import org.eclipse.kapua.gateway.client.Application;
import org.eclipse.kapua.gateway.client.Payload;
import org.eclipse.kapua.gateway.client.Sender;
import org.eclipse.kapua.gateway.client.Topic;
import org.eclipse.kapua.gateway.client.mqtt.fuse.FuseClient;
import org.eclipse.kapua.gateway.client.profile.kura.KuraMqttProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.eclipse.kapua.gateway.client.Credentials.userAndPassword;
import static org.eclipse.kapua.gateway.client.Errors.handle;
import static org.eclipse.kapua.gateway.client.Transport.waitForConnection;

/**
 * Description..
 */
public class Client {

    private Company company;
    private Economy economy;
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public Client(Economy economy, Company company) {
        this.company = company;
        this.economy = economy;
        initCommuniction();
    }

    private void initCommuniction() {
        try (org.eclipse.kapua.gateway.client.Client client = KuraMqttProfile.newProfile(FuseClient.Builder::new)
                .accountName("kapua-sys")
                .clientId("supply-chain-control-simulator")
                .brokerUrl("tcp://localhost:1883")
                .credentials(userAndPassword("kapua-broker", "kapua-password"))
                .build()) {

            try (Application application = client.buildApplication("app1").build()) {

                // wait for connection

                waitForConnection(application.transport());

                // subscribe to a topic

                application.data(Topic.of("my", "topic")).subscribe(message -> {
                    System.out.format("Received: %s%n", message);
                });

                // cache sender instance

                Sender<RuntimeException> sender = application
                        .data(Topic.of("my", "topic"))
                        .errors(handle((throwable, payload) -> logger.error("failed to publish"+throwable.getMessage())));

                int i = 0;
                while (true) {
                    // send
                    sender.send(Payload.of("counter", i++));
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
