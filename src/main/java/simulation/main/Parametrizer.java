package simulation.main;

import company.company.CompanyType;
import org.jetbrains.annotations.Nullable;
import simulation.simulators.SupplyChainControlSimulator;
import simulation.simulators.runners.AbstractRunner;

/**
 * The class allows to totally parametrize the whole simulation at will.
 * @author Arthur Deschamps
 * @since 1.0
 * @see SupplyChainControlSimulator
 */
public class Parametrizer {

    // Speed of virtual time compare to real time
    private int timeFlow;

    // Delay for periodic data sending to kapua in seconds
    private long dataSendingDelay;

    // Decides if data from the company and the economy will be displayed
    private boolean displayMetrics;

    // If displayMetrics is true, delay at which metrics are displayed in seconds
    private long displayMetricsDelay;

    private CompanyType companyType;

    @Nullable
    private String companyName;



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

    public long getDataSendingDelay() {
        return dataSendingDelay;
    }

    public int getTimeFlow() {
        return timeFlow;
    }

}
