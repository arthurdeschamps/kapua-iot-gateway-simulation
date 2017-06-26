package simulation.main;

import company.company.Company;
import economy.Economy;
import kapua.gateway.KapuaGatewayClient;
import simulation.generators.CompanyGenerator;
import simulation.simulators.SupplyChainControlSimulator;

import java.util.concurrent.TimeUnit;

/**
 * Same as DefaultSimulation but also displays information about economy and company.
 *
 * @author Arthur Deschamps
 * @since 1.0
 */
public class DefaultSimulationWithMetrics {

    public static void main(String[] args) {
        // Create a default parametrizer
        Parametrizer parametrizer = new Parametrizer(100, TimeUnit.MICROSECONDS);
        // Generates an economy with default initial metrics
        Economy economy = new Economy();
        // Generates a random company with random data
        Company company = CompanyGenerator.generateLocalCompany();

        // Starts the simulation
        SupplyChainControlSimulator supplyChainControlSimulator = new SupplyChainControlSimulator(company, economy, parametrizer);
        supplyChainControlSimulator.start(true);

        // Starts the communication with kapua
        KapuaGatewayClient kapuaGatewayClient = new KapuaGatewayClient(company,3);
        kapuaGatewayClient.startCommunications();
    }
}
