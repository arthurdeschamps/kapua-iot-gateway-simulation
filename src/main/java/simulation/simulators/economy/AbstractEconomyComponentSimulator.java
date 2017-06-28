package simulation.simulators.economy;

import economy.Economy;
import simulation.util.ProbabilityUtils;

import java.util.Random;

/**
 * Model for an economy component simulator.
 * @since 1.0
 * @author Arthur Deschamps
 */
public abstract class AbstractEconomyComponentSimulator implements Runnable {

    protected Economy economy;
    protected final Random random = new Random();
    private final ProbabilityUtils probabilityUtils = new ProbabilityUtils();

    public AbstractEconomyComponentSimulator(Economy economy) {
        this.economy = economy;
    }

}
