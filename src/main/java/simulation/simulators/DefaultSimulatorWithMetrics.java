package simulation.simulators;

import company.company.Company;
import economy.Economy;
import kapua.gateway.KapuaGatewayClient;
import simulation.SupplyChainControlSimulation;
import simulation.generator.CompanyGenerator;
import simulation.Parametrizer;

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
        SupplyChainControlSimulation supplyChainControlSimulation = new SupplyChainControlSimulation(company, economy, parametrizer);
        supplyChainControlSimulation.start(true);

        // Starts the communication with kapua
        KapuaGatewayClient kapuaGatewayClient = new KapuaGatewayClient(company,3);
        kapuaGatewayClient.startCommunications();
    }
}
