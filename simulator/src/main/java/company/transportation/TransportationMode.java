package company.transportation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Arthur Deschamps on 02.06.17.
 * Types of transportation
 */
public enum TransportationMode {
    AIR,WATER,LAND_RAIL,LAND_ROAD;

    private static final List<TransportationMode> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static TransportationMode randomTransportationMode()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
