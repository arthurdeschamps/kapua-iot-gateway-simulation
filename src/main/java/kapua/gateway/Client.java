package kapua.gateway;

import kapua.gateway.*;
import org.eclipse.kapua.gateway.client.Application;
import org.eclipse.kapua.gateway.client.Payload;
import org.eclipse.kapua.gateway.client.Sender;
import org.eclipse.kapua.gateway.client.Topic;
import org.eclipse.kapua.gateway.client.mqtt.fuse.FuseClient;

/**
 * Description..
 *
 * @author Arthur Deschamps
 */
public class Client {

    public Client() {
        try (Client client = KuraMqttProfile.newProfile(FuseClient.Builder::new)
                .accountName("kapua-sys")
                .clientId("foo-bar-1")
                .brokerUrl("tcp://localhost:1883")
                .credentials(userAndPassword("kapua-broker", "kapua-password"))
                .build()) {

            try (Application application = client.buildApplication("app1").build()) {

                // subscribe to a topic

                application.data(Topic.of("my", "receiver")).subscribe(message -> {
                    System.out.format("Received: %s%n", message);
                });

                // cache sender instance

                Sender<RuntimeException> sender = application
                        .data(Topic.of("my", "sender"))
                        .errors(ignore());

                int i = 0;
                while (true) {
                    // send
                    sender.send(Payload.of("counter", i++));
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
