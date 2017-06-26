package simulation.simulators.runners;

import company.company.Company;
import simulation.simulators.telemetry.AbstractTelemetryComponentSimulator;
import simulation.simulators.telemetry.TransportationHealthStateSimulator;
import simulation.simulators.telemetry.DeliveryMovementSimulator;

/**
 * Simulates different events related to telemetry data.
 * @author Arthur Deschamps
 * @since 1.0
 */
public class TelemetryDataSimulatorRunner extends AbstractRunner<AbstractTelemetryComponentSimulator> {

    public TelemetryDataSimulatorRunner(Company company) {
        super(new AbstractTelemetryComponentSimulator[] {
                new DeliveryMovementSimulator(company),
                new TransportationHealthStateSimulator(company)
        });
    }
}
