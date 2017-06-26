package company.transportation;

import storage.Item;

/**
 * Created by Arthur Deschamps on 02.06.17.
 * Company transportation means
 */
public class Transportation extends Item {

    private float capacity; // in tonnes
    private int maxSpeed; // km/h
    private TransportationMode transportationMode;
    private TransportationHealthState healthState;

    public Transportation(float capacity, int maxSpeed, TransportationMode transportationMode) {
        this.capacity = capacity;
        this.maxSpeed = maxSpeed;
        this.transportationMode = transportationMode;
        this.healthState = TransportationHealthState.PERFECT;
    }

    @Override
    public boolean validate() {
        return ((capacity > 0) && (maxSpeed > 0));
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

    public TransportationHealthState getHealthState() {
        return healthState;
    }

    public void setHealthState(TransportationHealthState healthState) {
        this.healthState = healthState;
    }
}
