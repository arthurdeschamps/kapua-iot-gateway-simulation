package simulation.simulators.runners;

import simulation.simulators.AbstractSimulatorInterface;
import simulation.util.ProbabilityUtils;

import java.util.Arrays;

/**
 * Model of a runner, that is an object that shall be called periodically in this application.
 * A call to "run" is equivalent to 1 hour elapsed in virtual time.
 * @author Arthur Deschamps
 * @since 1.0
 */
public abstract class AbstractRunner<T extends AbstractSimulatorInterface> implements Runnable {

    protected T[] simulators;

    AbstractRunner(T[] simulators) {
        this.simulators = simulators;
    }

    /**
     * Calls every simulator.
     */
    @Override
    public void run() {
        try {
            Arrays.stream(simulators).forEach(AbstractSimulatorInterface::run);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A runner has a time unit: the virtual time theoretically elapsed at each of their execution.
     * @return
     * The time unit the runners have been thought for. Currently Hour.
     */
    public static ProbabilityUtils.TimeUnit getTimeUnit() {
        return ProbabilityUtils.TimeUnit.HOUR;
    }
}

