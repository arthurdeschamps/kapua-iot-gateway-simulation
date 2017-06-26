package simulation.simulators.runners;

import simulation.simulators.AbstractSimulatorInterface;

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
     * Calls every simulators.
     */
    @Override
    public void run() {
        Arrays.stream(simulators).forEach(AbstractSimulatorInterface::run);
    }

    /**
     * Every simulator that shall be called in the "run" method shall be in the returned array.
     * @return
     * An array of simulators.
     */
    public T[] getSimulators() {
        return simulators;
    }

    public void setSimulators(T[] simulators) {
        this.simulators = simulators;
    }
}

