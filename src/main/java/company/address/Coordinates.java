package company.address;

/**
 * Describes a coordinate. Can be instantiated with integers or string with the appropriate format.
 * @author Arthur Deschamps
 * @since 1.0
 */
public class Coordinates {

    private float latitude;
    private float longitude;

    public Coordinates(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Coordinates(String latitude, String longitude) {
        try {
            this.latitude = Float.parseFloat(latitude);
            this.longitude = Float.parseFloat(longitude);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            this.latitude = 0;
            this.longitude = 0;
        }
    }

    /**
     * Used to clone the coordinates.
     * @param coordinates
     * Coordinates to clone.
     */
    public Coordinates(Coordinates coordinates) {
        this.latitude = coordinates.getLatitude();
        this.longitude = coordinates.getLongitude();
    }

    /**
     * Calculate new coordinates by moving the given distance from basePoint.
     * @param distanceX
     * An east/west distance in kilometers.
     * @param distanceY
     * A north/south distance in kilometers.
     * @param basePoint
     * The initial coordinate.
     * @return
     * The coordinates obtained by applying the distances from the base point.
     */
    public static Coordinates applyDistance(Coordinates basePoint, float distanceX, float distanceY) {
        final double oneDegreeLatitudeInKm = 110.54;
        final double oneDegreeLongitudeInKm = 111.32*Math.cos(Math.toRadians(basePoint.latitude));

        final float newLatitude = basePoint.latitude + distanceY/(float)oneDegreeLatitudeInKm;
        final float newLongitude = basePoint.longitude + distanceX/((float)oneDegreeLongitudeInKm);

        return new Coordinates(newLatitude,newLongitude);
    }

    /**
     * Calculates the distance between two coordinates.
     * @param p1
     * First coordinates.
     * @param p2
     * Second coordinates.
     * @return
     * Distance between p1 and p2 in km.
     */
    public static double calculateDistance(Coordinates p1, Coordinates p2) {
        final int earthRadiusInKm = 6371;
        final double dLat = Math.toRadians(p2.getLatitude()-p1.getLatitude());
        final double dLon = Math.toRadians(p2.getLongitude()-p1.getLongitude());

        final double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.sin(dLon/2) * Math.sin(dLon/2)
                * Math.cos(Math.toRadians(p1.getLatitude())) * Math.cos(Math.toRadians(p2.getLatitude()));
        return earthRadiusInKm * 2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinates that = (Coordinates) o;

        if (Float.compare(that.getLatitude(), getLatitude()) != 0) return false;
        return Float.compare(that.getLongitude(), getLongitude()) == 0;
    }

    @Override
    public int hashCode() {
        int result = (getLatitude() != +0.0f ? Float.floatToIntBits(getLatitude()) : 0);
        result = 31 * result + (getLongitude() != +0.0f ? Float.floatToIntBits(getLongitude()) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Latitude: "+this.getLatitude()+", Longitude: "+this.getLongitude();
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
