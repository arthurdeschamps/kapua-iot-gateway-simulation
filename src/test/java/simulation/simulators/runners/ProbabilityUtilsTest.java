package simulation.simulators.runners;

import org.junit.Assert;
import org.junit.Test;
import simulation.util.ProbabilityUtils;

/**
 * Created by Arthur Deschamps on 16.06.17.
 */
public class ProbabilityUtilsTest {

    private final ProbabilityUtils proba = new ProbabilityUtils();

    @Test
    public void testEvent() {
        // Converts units to seconds (what "event" simulates) makes sure "true" is returned at the actual frequency
        boolean occurred = false;

        // Test once an hour = 3600 seconds
        for (int i = 0; i < 3600 * 2; i++) {
            if (proba.event(1, ProbabilityUtils.TimeUnit.HOUR)) {
                occurred = true;
                break;
            }
        }

        Assert.assertTrue(occurred);
        occurred = false;

        // Test once a day = 3600 * 24
        for (int i = 0; i < 3600 * 24 * 2; i++) {
            if (proba.event(1, ProbabilityUtils.TimeUnit.DAY)) {
                occurred = true;
                break;
            }
        }

        Assert.assertTrue(occurred);
        occurred = false;

        // Test once a week = 3600*24*7
        for (int i = 0; i < 3600*24*7*2; i++) {
            if (proba.event(1, ProbabilityUtils.TimeUnit.WEEK)) {
                occurred = true;
                break;
            }
        }

        Assert.assertTrue(occurred);
    }

}