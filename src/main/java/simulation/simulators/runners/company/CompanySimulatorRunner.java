package simulation.simulators.runners.company;

import company.company.Company;
import economy.Economy;
import simulation.simulators.runners.economy.EconomySimulatorRunner;

import java.util.ArrayList;

/**
 * This runnable simulates everything related to an object of type Company.
 * The base unit of this runnable is 1 second. This means that 1 execution of the method run is equivalent to 1 second
 * elapsed in real time.
 *
 * @author Arthur Deschamps
 * @since 1.0
 * @see EconomySimulatorRunner
 */
public class CompanySimulatorRunner implements Runnable {

    private Company company;
    private Economy economy;

    private ArrayList<CompanyComponentSimulator> simulators;

    public CompanySimulatorRunner(Company company, Economy economy) {
        this.company = company;
        this.economy = economy;

        simulators = new ArrayList<>();
        simulators.add(new CustomerSimulator(company, economy));
        simulators.add(new ProductSimulator(company, economy));
        simulators.add(new OrderSimulator(company, economy));
        simulators.add(new DeliverySimulator(company, economy));
        simulators.add(new TransportationSimulator(company, economy));
    }

    /**
     * Calls every simulators.
     * @since 1.0
     */
    @Override
    public void run() {
        try {
            simulators.forEach(CompanyComponentSimulator::run);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Economy getEconomy() {
        return economy;
    }
}
