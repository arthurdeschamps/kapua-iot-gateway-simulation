package simulation.simulators.runners;

import company.company.Company;
import economy.Economy;
import simulation.simulators.company.*;

/**
 * This runnable simulates everything related to an object of type Company.
 * The base unit of this runnable is 1 second. This means that 1 execution of the method run is equivalent to 1 second
 * elapsed in real time.
 *
 * @author Arthur Deschamps
 * @since 1.0
 * @see EconomySimulatorRunner
 */
public class CompanySimulatorRunner extends AbstractRunner<AbstractCompanyComponentSimulator> {

    public CompanySimulatorRunner(Company company, Economy economy) {

        super(new AbstractCompanyComponentSimulator[] {
                new CustomerSimulator(company, economy),
                new ProductSimulator(company, economy),
                new OrderSimulator(company, economy),
                new DeliverySimulator(company, economy),
                new TransportationSimulator(company, economy)
        });
    }
}
