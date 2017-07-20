package simulation.simulators.company;

import company.company.Company;
import economy.Economy;
import simulation.util.ProbabilityUtils;

/**
 * Common model of simulation for anything related to the company.
 * @since 1.0
 * @author Arthur Deschamps
 */
public abstract class AbstractCompanyComponentSimulator implements Runnable {

    protected ProbabilityUtils probabilityUtils;
    protected Company company;
    protected Economy economy;

    public AbstractCompanyComponentSimulator(Company company, Economy economy) {
        this.company = company;
        this.economy = economy;
        this.probabilityUtils = new ProbabilityUtils();
    }

}
