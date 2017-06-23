package simulator.simulator;

import java.util.concurrent.TimeUnit;

/**
 * Parameters for the SupplyChainControlSimulator.
 * @author Arthur Deschamps
 * @since 1.0
 * @see SupplyChainControlSimulator
 */
public class Parametrizer {

    //TODO parametrize everything

    // Time flow speediness in seconds per second
    private long timeFlow;
    private TimeUnit timeUnit;

    Parametrizer() {
        // Default timeFlow: 1 sec = 10 sec
        this.timeFlow = 10;
        this.timeUnit = TimeUnit.SECONDS;
    }

    public Parametrizer(long timeFlow, TimeUnit timeUnit) {
        this.timeFlow = timeFlow;
        this.timeUnit = timeUnit;
    }

    /**
     * Convert time flow (speed of time flow for the simulation) to delay for the runnable objects.
     * @return
     * timeFlow converted to a delay for the runnable objects. This delay is used to indicate the frequency at which
     * an Executor shall run our simulators.
     */
    public long getExecutorDelay() {
        long delay = (long) ((1/(double)timeFlow * Math.pow(10,6)));
        if (delay < 1)
            delay = 1;
        return delay;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public long getTimeFlow() {
        return timeFlow;
    }

    public void setTimeFlow(long timeFlow) {
        this.timeFlow = timeFlow;
    }
}
