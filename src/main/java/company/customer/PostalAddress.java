package company.customer;

import redis.clients.jedis.GeoCoordinate;

/**
 * Created by Arthur Deschamps on 03.06.17.
 */
public class PostalAddress {

    private String street;
    private String city;
    private String region;
    private String country;
    private int postalCode;

    public PostalAddress(String street, String city, String region, String country, int postalCode) {
        this.street = street;
        this.city = city;
        this.region = region;
        this.country = country;
        this.postalCode = postalCode;
    }

    public GeoCoordinate asCoordinates() {
        // TODO: address to coordinates conversion
        return new GeoCoordinate(0,0);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(PostalAddress.class)) {
            PostalAddress addr = (PostalAddress) obj;
            return (this.getCity().equals(addr.getCity())) && (this.getCountry().equals(addr.getCountry())) &&
                    (this.getPostalCode() == addr.getPostalCode()) && (this.getRegion().equals(addr.getRegion()))
                    && (this.getStreet().equals(addr.getStreet()));
        }
        return false;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
