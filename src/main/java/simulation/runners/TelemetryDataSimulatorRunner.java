package simulation.runners;

import company.company.Company;
import simulation.simulators.telemetry.AbstractTelemetryComponentSimulator;
import simulation.simulators.telemetry.DeliveryMovementSimulator;

/**
 * The base time unit of this class is 1 hour, that is every execution of run is equivalent to 3600 seconds elapsed in
 * simulation time.
 * @author Arthur Deschamps
 * @since 1.0
 */
public class TelemetryDataSimulatorRunner extends AbstractRunner<AbstractTelemetryComponentSimulator> {

    public TelemetryDataSimulatorRunner(Company company) {
        super(new AbstractTelemetryComponentSimulator[] {
                new DeliveryMovementSimulator(company)
        });
    }
}
