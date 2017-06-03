package company.transportation;

import company.transportation.TransportationMode;
import jedis.JedisManager;
import jedis.JedisObject;

/**
 * Created by Arthur Deschamps on 02.06.17.
 * Company transportation means
 */
public class Transportation extends JedisObject {

    private float capacity; // in tonnes
    private int maxSpeed; // km/h
    private TransportationMode transportationMode;


    public Transportation(float capacity, int maxSpeed, TransportationMode transportationMode) {
        this.capacity = capacity;
        this.maxSpeed = maxSpeed;
        this.transportationMode = transportationMode;
    }

    @Override
    public boolean validate() {
        return ((capacity > 0) && (maxSpeed > 0));
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

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public TransportationMode getTransportationMode() {
        return transportationMode;
    }
}
