package simulation.simulators.runners;

import economy.Economy;
import simulation.simulators.economy.*;

/**
 * Created by Arthur Deschamps on 02.06.17.
 * This class gives a frame to the simulation. The economy determines the sales made by the simulated company.
 * 1 call to run is equivalent to 1 second in "real" time
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
