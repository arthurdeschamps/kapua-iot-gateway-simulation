package simulator.main;

import company.main.Company;
import economy.Economy;
import kapua.gateway.Client;
import simulator.generator.CompanyGenerator;
import simulator.util.Parametrizer;

/**
 * Description..
 *
 * @author Arthur Deschamps
 */
public class Main {

    public static void main(String[] args) {
        // Create a default parametrizer TODO: let user choose the parameters
        Parametrizer parametrizer = new Parametrizer(100);
        // Generates an economy with default initial metrics
        Economy economy = new Economy();
        // Generates a random company with random data
        Company company = new CompanyGenerator().generateRandomCompany();

        // Starts the simulation
        new SupplyChainControlSimulator(company,economy,parametrizer);

        // Starts the communication with kapua
        Client client = new Client(economy,company);
        client.startPublicationsAndSubscriptions();
    }

}
