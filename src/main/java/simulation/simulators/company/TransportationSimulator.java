package simulation.simulators.company;

import company.company.Company;
import company.transportation.Transportation;
import company.transportation.TransportationHealthState;
import economy.Economy;
import simulation.generators.DataGenerator;
import simulation.util.ProbabilityUtils;

/**
 * Simulates everything related to transportation (except telemetry data)
 * @since 1.0
 * @author Arthur Deschamps
 */
public class TransportationSimulator extends AbstractCompanyComponentSimulator {

    public TransportationSimulator(Company company, Economy economy) {
        super(company, economy);
    }

    @Override
    public void run() {
        simulateTransportationAcquisitions();
        simulateTransportationDestruction();
    }

    /**
     * Simulates transportation acquisition
     * @since 1.0
     */
    private void simulateTransportationAcquisitions() {
        // If orders >= nbr transportation * 100, new transportation should be acquired
        if (company.getOrders().size() >= company.getAllTransportation().size()*10) {
            // On average takes 2 weeks to be done
            if (probabilityUtils.event(5, ProbabilityUtils.TimeUnit.WEEK)) {
                Transportation transportation = DataGenerator.generateRandomTransportation();
                company.newTransportation(transportation);
            }
        }
    }

    /**
     * Simulates transportation destruction
     * @since 1.0
     */
    private void simulateTransportationDestruction() {
        // A transportation is destructed when its in a bad state or worse
        company.getAllTransportation()
                .stream()
                .filter(transportation -> transportation.isAvailable())
                .forEach(transportation -> {
                    final TransportationHealthState state = transportation.getHealthState();

                    if ((state.equals(TransportationHealthState.BAD) &&
                            probabilityUtils.event(1, ProbabilityUtils.TimeUnit.DAY)) ||
                            state.equals(TransportationHealthState.CRITICAL))
                    {
                       company.deleteTransportation(transportation);
                    }
                });
    }

}
