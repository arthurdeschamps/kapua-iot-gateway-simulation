package simulation.simulators.runners;

import company.company.Company;
import economy.Economy;
import simulation.simulators.company.*;

/**
 * This runnable simulates everything related to an object of type Company.
 * @author Arthur Deschamps
 * @since 1.0
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
