package simulator.runner;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Arthur Deschamps on 16.06.17.
 */
public class ProbabilitySimulatorTest {

    private final ProbabilitySimulator proba = new ProbabilitySimulator();

    @Test
    public void testEvent() {
        // Converts units to seconds (what "event" simulates) makes sure "true" is returned at the actual frequency
        boolean occured = false;

        // Test once an hour = 3600 seconds
        for (int i = 0; i < 3600 * 2; i++) {
            if (proba.event(1, ProbabilitySimulator.TimeUnit.HOUR)) {
                occured = true;
                break;
            }
        }

        Assert.assertTrue(occured);
        occured = false;

        // Test once a day = 3600 * 24
        for (int i = 0; i < 3600 * 24 * 2; i++) {
            if (proba.event(1,ProbabilitySimulator.TimeUnit.DAY)) {
                occured = true;
                break;
            }
        }

        Assert.assertTrue(occured);
        occured = false;

        // Test once a week = 3600*24*7
        for (int i = 0; i < 3600*24*7*2; i++) {
            if (proba.event(1,ProbabilitySimulator.TimeUnit.WEEK)) {
                occured = true;
                break;
            }
        }

        Assert.assertTrue(occured);
    }

}