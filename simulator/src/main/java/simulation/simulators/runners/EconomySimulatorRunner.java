package simulation.simulators.runners;

import economy.Economy;
import simulation.simulators.economy.*;

/**
 * Simulates economy.
 * @author Arthur Deschamps
 * @since 1.0
 */
public class EconomySimulatorRunner extends AbstractRunner<AbstractEconomyComponentSimulator> {

    public EconomySimulatorRunner(Economy economy) {
        super(new AbstractEconomyComponentSimulator[] {
                new DemandSimulator(economy),
                new GrowthSimulator(economy),
                new SectorConcurrencySimulator(economy),
                new UpheavalSimulator(economy)
        });
    }

}
