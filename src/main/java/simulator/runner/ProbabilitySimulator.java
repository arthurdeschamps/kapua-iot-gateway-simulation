package simulator.runner;

import java.util.Random;

/**
 * Created by Arthur Deschamps on 13.06.17.
 */
class ProbabilitySimulator {
    /**
     * This method returns true if an event that happens with a certain frequency (in a certain time unit) happened
     * and false if it did not happen. The only computation done is a Random.nextInt(nbr) with nbr depending on the
     * frequency and time unit.
     * @param frequency
     * Frequency at which the event occurs.
     * @param timeUnit
     * The frequency's unit. See TimeUnit enumeration of this class.
     * @return
     * A boolean value indicating if the event occured (true) or not (false).
     *
     * @since 1.0
     */
    boolean event(double frequency, TimeUnit timeUnit) {
        if (frequency < 0)
            throw new IllegalArgumentException("Frequency can't be negative");
        // Convert frequency from initial time unit to the biggest available unit in order to make the decimal part of
        // the computed frequency not relevant in comparison to the integer part
        double normalizedFrequency = TimeUnit.convertToBiggestUnit(frequency,timeUnit);
        // Convert frequency per second to number of favorable outcomes for a uniform law of probability
        long nbrFavorableOutcomes = (long) normalizedFrequency;
        // We are simulating one second, so the total number of outcomes is the number of second in a year.
        final long nbrTotalOutcomes = 12*4*7*24*60*60;
        // Simulate event using a uniform law of probability
        Random random = new Random();
        return random.nextInt((int)(nbrTotalOutcomes+1-nbrFavorableOutcomes)) == 0;
    }

    /**
     * TimeUnit allows simulation functions to indicate the time unit of the frequency at each the simulated event
     * occurs on average.
     * @since 1.0
     */
     enum TimeUnit {
        HOUR, DAY, WEEK, MONTH, YEAR;

        public static double convertToBiggestUnit(double value, TimeUnit timeUnit) {
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
            }
            throw new EnumConstantNotPresentException(TimeUnit.class,"Given TimeUnit not recognized");
        }
    }
}
