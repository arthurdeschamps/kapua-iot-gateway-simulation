package simulation.simulators.runners.company;

import company.company.Company;
import economy.Economy;
import simulation.util.ProbabilityUtils;

/**
 * Common model of simulation for anything related to the company.
 * @since 1.0
 * @author Arthur Deschamps
 */
abstract class CompanyComponentSimulator {

    protected Company company;
    protected Economy economy;
    protected ProbabilityUtils probabilityUtils;

    CompanyComponentSimulator(Company company, Economy economy) {
        this.company = company;
        this.economy = economy;
        this.probabilityUtils = new ProbabilityUtils();
    }

    abstract void run();
}
