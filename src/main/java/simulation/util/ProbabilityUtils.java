package simulation.util;

import java.util.Random;

/**
 * Created by Arthur Deschamps on 13.06.17.
 * @author Arthur Deschamps
 * @since 1.0
 */
public class ProbabilityUtils {
    /**
     * This method simulates if an event that occurs at the given frequency in the given time unit has occurred in 1 second.
     * For instance, if the event has frequency 1 per hour, the method might be called on average 3600 times in order to return true
     * at least once.
     * @param frequency
     * Frequency at which the event occurs.
     * @param timeUnit
     * The frequency's unit. See TimeUnit enumeration of this class.
     * @return
     * A boolean value indicating if the event occured (true) or not (false).
     *
     * @since 1.0
     */
    public boolean event(double frequency, TimeUnit timeUnit) {
        if (frequency < 0)
            throw new IllegalArgumentException("Frequency can't be negative");
        // Convert frequency from initial time unit to the biggest available unit in order to make the decimal part of
        // the computed frequency not relevant in comparison to the integer part
        double normalizedFrequency = TimeUnit.scaleToBiggestUnit(frequency,timeUnit);
        // Convert frequency per second to number of favorable outcomes for a uniform law of probability
        long nbrFavorableOutcomes = (long) normalizedFrequency;
        // We are simulating one second, so the total number of outcomes one second converted to the biggest unit
        final double nbrTotalOutcomes = TimeUnit.scaleToBiggestUnit(1,TimeUnit.SECOND);
        // Simulate event using a uniform law of probability
        Random random = new Random();
        return random.nextInt((int)(nbrTotalOutcomes/nbrFavorableOutcomes)) == 0;
    }

    /**
     * TimeUnit allows simulation functions to indicate the time unit of the frequency at each the simulated event
     * occurs on average.
     * @since 1.0
     */
     public enum TimeUnit {
        SECOND, MINUTE, HOUR, DAY, WEEK, MONTH, YEAR;

        /**
         * Scales a given value in time unit to a new value in the biggest existing unit (ex: 1 month = 12 if year is
         * the biggest unit).
         * @param value
         * Frequency
         * @param timeUnit
         * Frequency's time unit
         * @return
         * Frequency scaled to the biggest time unit of TimeUnit
         */
        public static double scaleToBiggestUnit(double value, TimeUnit timeUnit) {
            // Biggest unit is currently a year
            switch (timeUnit) {
                case YEAR:
                    return value;
                case MONTH:
                    return value*12;
                case WEEK:
                    return value*12*4;
                case DAY:
                    return value*12*4*7;
                case HOUR:
                    return value*12*4*7*24;
                case MINUTE:
                    return value*12*4*7*24*60;
                case SECOND:
                    return value*12*4*7*24*60*60;
            }
            throw new EnumConstantNotPresentException(TimeUnit.class,"Given TimeUnit not recognized");
        }
    }
}
