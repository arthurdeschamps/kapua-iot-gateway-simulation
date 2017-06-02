package company.main;

import redis.clients.jedis.GeoCoordinate;

/**
 * Created by Arthur Deschamps on 30.05.17.
 */
public abstract class Company {

    private GeoCoordinate location;

    public Company(GeoCoordinate location) {
        this.location = location;
    }

    public GeoCoordinate getLocation() {
        return location;
    }

    public void setLocation(GeoCoordinate location) {
        this.location = location;
    }
}
