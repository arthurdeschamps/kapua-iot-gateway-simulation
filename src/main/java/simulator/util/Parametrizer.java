package simulator.util;

import simulator.main.SupplyChainControlSimulator;

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

    Parametrizer() {
        // Default timeFlow: 1 sec = 10 sec
        this.timeFlow = 10;
    }

    public Parametrizer(long timeFlow) {
        this.timeFlow = timeFlow;
    }

    public long getTimeFlow() {
        return timeFlow;
    }

    public void setTimeFlow(long timeFlow) {
        this.timeFlow = timeFlow;
    }
}
