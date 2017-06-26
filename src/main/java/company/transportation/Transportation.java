package company.transportation;

import storage.Item;

import java.util.Random;

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

    /**
     * Calculates the actual speed of the transportation. The calculation involves randomness to make it more
     * realistic.
     * @return
     * A float representing a speed in km/s
     */
    public float getActualSpeed() {
        double speed = (double)this.getMaxSpeed()/(double) 3600; // Converts km/h to km/s

        // Includes a randomness factor (speed is not constant during a delivery process) in the form of a bonus or
        // penalty from 1 to 5 %.
        Random random = new Random();
        final int randomFactor = (int) Math.pow(-1,random.nextInt(2))*(random.nextInt(5)+1);

        return (float) (speed+speed*randomFactor/100);
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
