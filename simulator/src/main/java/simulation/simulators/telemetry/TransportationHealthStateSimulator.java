package simulation.simulators.telemetry;

import company.company.Company;
import company.transportation.Transportation;
import simulation.util.ProbabilityUtils;

/**
 * Description..
 * @since 1.0
 * @author Arthur Deschamps
 */
public class TransportationHealthStateSimulator extends AbstractTelemetryComponentSimulator {

    public TransportationHealthStateSimulator(Company company) {
        super(company);
    }

    @Override
    public void run() {
        company.getAllTransportation().forEach(this::simulateHealthStateDegradation);
    }

    /**
     * Simulates transportation health state degradation throughout time.
     * @param transportation
     * Transportation to degrade.
     */
    private void simulateHealthStateDegradation(Transportation transportation) {
        if (probabilityUtils.event(1, ProbabilityUtils.TimeUnit.MONTH))
            transportation.degradeHealthState();
    }
}
