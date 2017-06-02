package company;

import jedis.JedisObject;
import redis.clients.jedis.GeoCoordinate;

import java.util.UUID;

/**
 * Created by Arthur Deschamps on 02.06.17.
 * Company transportation means
 */
public class Transportation extends JedisObject {

    private String id;
    private float capacity; // in tonnes
    private GeoCoordinate currentPosition;
    private int maxSpeed; // km/h

    public Transportation(float capacity, GeoCoordinate currentPosition, int maxSpeed) {
        this.capacity = capacity;
        this.currentPosition = currentPosition;
        this.maxSpeed = maxSpeed;
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public boolean validate() {
        return ((capacity > 0) && (currentPosition != null) && (maxSpeed > 0));
    }

    @Override
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getCapacity() {
        return capacity;
    }

    public void setCapacity(float capacity) {
        this.capacity = capacity;
    }

    public GeoCoordinate getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(GeoCoordinate currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
}
