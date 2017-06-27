package simulation.main;

import company.company.Company;
import economy.Economy;
import kapua.gateway.KapuaGatewayClient;
import simulation.generators.CompanyGenerator;
import simulation.simulators.SupplyChainControlSimulator;

/**
 * Same as DefaultSimulation but also displays information about economy and company.
 *
 * @author Arthur Deschamps
 * @since 1.0
 */
public class DefaultSimulationWithMetrics {

    public static void main(String[] args) {
        // Create a default parametrizer
        Parametrizer parametrizer = new Parametrizer(100,3);
        // Generates an economy with default initial metrics
        Economy economy = new Economy();
        // Generates a random company with random data
        Company company = CompanyGenerator.generateLocalCompany();

        // Starts the simulation
        new SupplyChainControlSimulator(company, economy, parametrizer).start(true);

        // Starts sending data
        new KapuaGatewayClient(company,parametrizer.getDataSendingDelay()).startCommunication();
    }
}
