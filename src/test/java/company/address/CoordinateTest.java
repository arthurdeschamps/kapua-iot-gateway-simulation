package company.address;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests CoordinateTest class
 *
 * @author Arthur Deschamps
 */
public class CoordinateTest {

    @Test
    public void testConverter() {
        String latitude = "-1.33984";
        String longitude = "49.33389";
        Coordinate coordinate = new Coordinate(latitude,longitude);
        Assert.assertTrue(-1.33984f == coordinate.getLatitude());
        Assert.assertTrue(49.33389f == coordinate.getLongitude());
    }

    @Test
    public void testDistanceCalculator() {
        Assert.assertTrue(Coordinate.calculateDistance(new Coordinate(0f,0f),
                new Coordinate(0f,0f)) == 0);

        Coordinate p1 = new Coordinate(42.990967f, -71.463767f);
        Coordinate p2 = new Coordinate(50.000000f, 34.9588f);
        Assert.assertEquals(Coordinate.calculateDistance(p1,p2),Coordinate.calculateDistance(p2,p1),1e-15);
        Assert.assertEquals(Coordinate.calculateDistance(p1,p2),7480,50);


    }
}
