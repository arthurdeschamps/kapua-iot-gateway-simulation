package simulator.main;

import company.company.Company;
import economy.Economy;
import kapua.gateway.KapuaGatewayClient;
import simulator.generator.CompanyGenerator;
import simulator.simulator.Parametrizer;
import simulator.simulator.SupplyChainControlSimulator;

import java.util.concurrent.TimeUnit;

/**
 * Same as DefaultSimulator but also displays information about economy and company.
 *
 * @author Arthur Deschamps
 * @since 1.0
 */
public class DefaultSimulatorWithMetrics {

    public static void main(String[] args) {
        // Create a default parametrizer
        Parametrizer parametrizer = new Parametrizer(100, TimeUnit.MICROSECONDS);
        // Generates an economy with default initial metrics
        Economy economy = new Economy();
        // Generates a random company with random data
        Company company = new CompanyGenerator().generateRandomCompany();

        // Starts the simulation
        SupplyChainControlSimulator supplyChainControlSimulator = new SupplyChainControlSimulator(company, economy, parametrizer);
        supplyChainControlSimulator.start(true);

        // Starts the communication with kapua
        KapuaGatewayClient kapuaGatewayClient = new KapuaGatewayClient(company,3);
        kapuaGatewayClient.startCommunications();
    }
}
