package company.address;

/**
 * Describes a coordinate. Can be instantiated with integers or string with the appropriate format.
 * @author Arthur Deschamps
 * @since 1.0
 */
public class Coordinate {

    private float latitude;
    private float longitude;

    public Coordinate(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Coordinate(String latitude, String longitude) {
        try {
            this.latitude = Float.parseFloat(latitude);
            this.longitude = Float.parseFloat(longitude);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            this.latitude = 0;
            this.longitude = 0;
        }
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }
}
