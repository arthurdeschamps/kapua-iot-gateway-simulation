package company.delivery;

import company.product.Product;
import company.transportation.Transportation;
import jedis.JedisManager;
import jedis.JedisObject;
import redis.clients.jedis.GeoCoordinate;

import java.util.List;

/**
 * Created by Arthur Deschamps on 02.06.17.
 */
public class Delivery extends JedisObject {

    private String id;
    private List<Product> packageContent;
    private Transportation transporter;
    private GeoCoordinate destination;
    private GeoCoordinate departure;
    private GeoCoordinate currentPosition;
    private DeliveryStatus deliveryState;

    public Delivery(List<Product> packageContent, Transportation transporter, GeoCoordinate departure, GeoCoordinate currentPosition, GeoCoordinate destination) {
        this.id = JedisManager.getInstance().generateUniqueId();
        this.packageContent = packageContent;
        this.transporter = transporter;
        this.departure = departure;
        this.currentPosition = currentPosition;
        this.departure = departure;
        this.destination = destination;
        this.deliveryState = DeliveryStatus.WAREHOUSE;
    }

    @Override
    public boolean validate() {
        return ((packageContent.size() > 0) && (transporter != null) && (destination != null) &&
                (departure != null) && (currentPosition != null) && (deliveryState != null));
    }

    @Override
    public String getId() {
        return id;
    }

    public List<Product> getPackageContent() {
        return packageContent;
    }

    public void setPackageContent(List<Product> packageContent) {
        this.packageContent = packageContent;
    }

    public Transportation getTransporter() {
        return transporter;
    }

    public void setTransporter(Transportation transporter) {
        this.transporter = transporter;
    }

    public GeoCoordinate getDestination() {
        return destination;
    }

    public void setDestination(GeoCoordinate destination) {
        this.destination = destination;
    }

    public GeoCoordinate getDeparture() {
        return departure;
    }

    public void setDeparture(GeoCoordinate departure) {
        this.departure = departure;
    }

    public GeoCoordinate getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(GeoCoordinate currentPosition) {
        this.currentPosition = currentPosition;
    }

    public DeliveryStatus getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(DeliveryStatus deliveryState) {
        this.deliveryState = deliveryState;
    }
}
