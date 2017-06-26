package simulation.simulators.company;

import company.company.Company;
import company.transportation.Transportation;
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
        if (company.getOrders().size() >= company.getAllTransportation().size()*100) {
            // On average takes 2 weeks to be done
            if (probabilityUtils.event(0.5d, ProbabilityUtils.TimeUnit.WEEK)) {
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
        // If number of transportation surpasses number of orders, there is a surplus of transportation
        if (company.getOrders().size() <= company.getAllTransportation().size()) {
            // Takes on average two weeks to get rid of
            if (probabilityUtils.event(2, ProbabilityUtils.TimeUnit.WEEK)) {
                company.getAvailableTransportation().ifPresent(transportation ->
                        company.deleteTransportation(transportation));
            }
        }
    }

}
