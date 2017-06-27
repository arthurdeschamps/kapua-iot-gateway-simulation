package simulation.main;

import simulation.simulators.SupplyChainControlSimulator;
import simulation.simulators.runners.AbstractRunner;

/**
 * Parameters for the SupplyChainControlSimulator.
 * @author Arthur Deschamps
 * @since 1.0
 * @see SupplyChainControlSimulator
 */
public class Parametrizer {
    // Speed of virtual time compare to real time
    private int timeFlow;

    // Delay for periodic data sending to kapua
    private int dataSendingDelay;

    public Parametrizer() {
        // Default time flow is 10 times faster
        timeFlow = 10;
        // Default data sending delay is 1 second
        dataSendingDelay = 1;
    }

    public Parametrizer(int timeFlow, int dataSendingDelay) {
        this.timeFlow = timeFlow;
        this.dataSendingDelay = dataSendingDelay;
    }

    public long getDelayInMicroSeconds() {
        switch (AbstractRunner.getTimeUnit()) {
            case HOUR:
                // If one run represents 1 hour, then the time is already multiplied by 60^2
                return ((long)(Math.pow(10,6))*(long)timeFlow)/(long)(Math.pow(60,2));
            default:
                throw new UnsupportedOperationException("The only supported time unit for runners is Hour.");
        }
    }

    public int getDataSendingDelay() {
        return dataSendingDelay;
    }

    public void setDataSendingDelay(int dataSendingDelay) {
        this.dataSendingDelay = dataSendingDelay;
    }

    public int getTimeFlow() {
        return timeFlow;
    }

    public void setTimeFlow(int timeFlow) {
        this.timeFlow = timeFlow;
    }
}
