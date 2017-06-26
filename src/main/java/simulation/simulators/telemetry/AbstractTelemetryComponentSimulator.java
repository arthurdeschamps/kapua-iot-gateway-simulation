package simulation.simulators.telemetry;

import company.company.Company;
import simulation.simulators.AbstractSimulatorInterface;

/**
 * Model for a telemetry simulator.
 *
 * @author Arthur Deschamps
 */
public abstract class AbstractTelemetryComponentSimulator implements AbstractSimulatorInterface {

    protected Company company;

    AbstractTelemetryComponentSimulator(Company company) {
        this.company = company;
    }
}
