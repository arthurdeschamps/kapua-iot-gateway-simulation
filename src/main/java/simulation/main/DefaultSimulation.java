package simulation.main;

import company.company.Company;
import economy.Economy;
import kapua.gateway.KapuaGatewayClient;
import simulation.generators.CompanyGenerator;
import simulation.simulators.SupplyChainControlSimulator;

/**
 * Default simulation. Runs economy, company and delivery transport simulations and sends data to kapua.
 * @author Arthur Deschamps
 * @since 1.0
 */
public class DefaultSimulation {

    public static void main(String[] args) {
        // Create a default parametrizer with virtual time 100 times faster than real time
        Parametrizer parametrizer = new Parametrizer(100,3);
        // Generates an economy with default initial metrics
        Economy economy = new Economy();
        // Generates a random company with random data
        Company company = CompanyGenerator.generateLocalCompany();

        // Starts the simulation
        new SupplyChainControlSimulator(company,economy,parametrizer).start(false);

        // Start sending data
        new KapuaGatewayClient(company,parametrizer.getDataSendingDelay()).startCommunication();
    }

}
