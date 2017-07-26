package communications.kapua;

import company.company.Company;
import org.eclipse.kapua.gateway.client.Application;
import org.eclipse.kapua.gateway.client.mqtt.paho.PahoClient;
import org.eclipse.kapua.gateway.client.profile.kura.KuraMqttProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.eclipse.kapua.gateway.client.Credentials.userAndPassword;
import static org.eclipse.kapua.gateway.client.Transport.waitForConnection;

/**
 * Interface and communicate with Kapua
 * @author Arthur Deschamps
 * @since 1.0
 */
public class KapuaClient {

    private Company company;
    private org.eclipse.kapua.gateway.client.Client client;
    private Application application;

    private long communicationsDelay;
    private final int port = 1883;
    private final String host = "localhost";
    private final String accountName = "kapua-sys";
    private final String applicationId = "kapua-iot-gateway-simulation-scm";
    private final String clientId = "supply-chain-control-simulation";

    private static final Logger logger = LoggerFactory.getLogger(KapuaClient.class);

    public KapuaClient(Company company, final long communicationsDelay) {
        this.company = company;
        this.communicationsDelay = communicationsDelay;

        try {
            client = KuraMqttProfile.newProfile(PahoClient.Builder::new)
                    .accountName(accountName)
                    .clientId(clientId)
                    .brokerUrl("tcp://"+host+":"+Integer.toString(port))
                    .credentials(userAndPassword(accountName, "kapua-password"))
                    .build();

        } catch (Exception e) {
           logger.error(e.getMessage());
        }

        application = client.buildApplication(applicationId).build();

    }

    /**
     * Starts sending data to kapua.
    **/
    public void startCommunications() {
        try {
            // Wait for connection
            waitForConnection(application.transport());
            // Start sending data
            Executors.newSingleThreadScheduledExecutor()
                    .scheduleWithFixedDelay(
                            new DataSenderRunner(company,application),
                            0,
                            communicationsDelay,
                            TimeUnit.SECONDS
                    );
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

}
