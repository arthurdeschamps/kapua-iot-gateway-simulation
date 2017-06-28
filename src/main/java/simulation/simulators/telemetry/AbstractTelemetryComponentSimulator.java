package simulation.simulators.telemetry;

import company.company.Company;
import simulation.util.ProbabilityUtils;

/**
 * Model for a telemetry simulator.
 * @since 1.0
 * @author Arthur Deschamps
 */
public abstract class AbstractTelemetryComponentSimulator implements Runnable {

    protected Company company;
    protected final ProbabilityUtils probabilityUtils = new ProbabilityUtils();

    AbstractTelemetryComponentSimulator(Company company) {
        this.company = company;
    }
}
