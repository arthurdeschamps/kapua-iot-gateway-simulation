/**
 * @since 1.0
 * @author Arthur Deschamps
 */

public class Main {

    public static void main(String[] args) {

        final int port = 1883;
        final String host = "localhost";
        new MqttSubscriptionsManager(host,port).startListening();
    }
}
