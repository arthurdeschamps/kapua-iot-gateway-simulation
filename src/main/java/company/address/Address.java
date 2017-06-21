package company.address;

/**
 * Describes an address (street, city, country, zip, coordinates, etc).
 * @author Arthur Deschamps
 * @since 1.0
 */
public class Address {

    private String street;
    private String city;
    private String region;
    private String country;
    private String zip;
    private Coordinate coordinate;

    public Address(String street, String city, String region, String country, String zip, Coordinate coordinate) {
        this.street = street;
        this.city = city;
        this.region = region;
        this.country = country;
        this.zip = zip;
        this.coordinate = coordinate;
    }

    @Override
    public String toString() {
        return this.getZip()+" "+this.getStreet()+" "+this.getCity()+" "+this.getRegion()+" "+this.getCountry();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(Address.class)) {
            Address addr = (Address) obj;
            return (this.getCity().equals(addr.getCity())) && (this.getCountry().equals(addr.getCountry())) &&
                    (this.getZip() == addr.getZip()) && (this.getRegion().equals(addr.getRegion()))
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

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
}
