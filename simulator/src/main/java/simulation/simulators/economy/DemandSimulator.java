package simulation.simulators.economy;

import economy.Economy;

/**
 * Simulates market demand
 * @since 1.0
 * @author Arthur Deschamps
 */
public class DemandSimulator extends AbstractEconomyComponentSimulator {

    public DemandSimulator(Economy economy) {
        super(economy);
    }

    @Override
    public void run() {
        simulateDemand();
    }

    private void simulateDemand() {
        /*
         Demand is a result of sector concurrency and economy growth but only reflects once in a while
         demand = demand + (growth/2 - concurrency)/100
          */
        if (random.nextInt(1000) == 0)
            economy.setDemand(Math.abs(economy.getDemand()+(economy.getGrowth()*0.1f-economy.getSectorConcurrency())/100));
    }
}
