package simulation.simulators;

import company.company.Company;
import economy.Economy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simulation.main.Parametrizer;
import simulation.simulators.runners.CompanySimulatorRunner;
import simulation.simulators.runners.EconomySimulatorRunner;
import simulation.simulators.runners.TelemetryDataSimulatorRunner;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Simulates the supply chain control for a company.
 * @author Arthur Deschamps
 * @since 1.0
 */
public class SupplyChainControlSimulator {

    private  Company company;
    private Economy economy;
    private Parametrizer parametrizer;

    private  EconomySimulatorRunner economySimulator;
    private  CompanySimulatorRunner companySimulator;
    private TelemetryDataSimulatorRunner telemetrySimulator;

    private  final Logger logger = LoggerFactory.getLogger(SupplyChainControlSimulator.class);

    public SupplyChainControlSimulator(Parametrizer parametrizer) {
        this.parametrizer = parametrizer;
        this.economy = parametrizer.getEconomy();
        this.company = parametrizer.getCompany();
        this.economySimulator = new EconomySimulatorRunner(economy);
        this.companySimulator = new CompanySimulatorRunner(company,economy);
        this.telemetrySimulator = new TelemetryDataSimulatorRunner(company);
    }

    /**
     * Starts simulations. If showMetrics is true, also starts threads that will display data.
     */
    public void start() {
        try {
            ScheduledExecutorService simulatorsExecutor = Executors.newSingleThreadScheduledExecutor();

            simulatorsExecutor.scheduleWithFixedDelay(economySimulator,0,parametrizer.getDelayInMicroSeconds(),
                    TimeUnit.MICROSECONDS);
            simulatorsExecutor.scheduleWithFixedDelay(companySimulator,0,parametrizer.getDelayInMicroSeconds(),
                    TimeUnit.MICROSECONDS);
            simulatorsExecutor.scheduleWithFixedDelay(telemetrySimulator,0,parametrizer.getDelayInMicroSeconds(),
                    TimeUnit.MICROSECONDS);

            logger.info("Simulation started");

            if (parametrizer.isDisplayMetrics()) {
                ScheduledExecutorService metricsExecutorService = Executors.newScheduledThreadPool(2);
                displayEconomicalData(metricsExecutorService);
                displayCompanyData(metricsExecutorService);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Display information on economics (growth, demand, etc).
     * @param executor
     * ScheduledExecutorService to attach the thread to.
     */
    private void displayEconomicalData(ScheduledExecutorService executor) {
        executor.scheduleWithFixedDelay(() -> logger.info("Growth: "+economy.getGrowth()+", Demand: "+economy.getDemand()
                +", Sector concurrency: "+economy.getSectorConcurrency()),1,5,TimeUnit.SECONDS);
    }

    /**
     * Display information on company (products, orders, etc).
     * @param executor
     * ScheduledExecutorService to attach the thread to.
     */
    private void displayCompanyData(ScheduledExecutorService executor) {
        executor.scheduleWithFixedDelay(() -> logger.info("Products: "+Integer.toString(company.getProducts().size())+", Types: "+
                Integer.toString(company.getProductTypes().size())+
                ", Orders: "+Integer.toString(company.getOrders().size())+", Deliveries: "+Integer.toString(company.getDeliveries().size())
                +", Transportation: "+Integer.toString(company.getAllTransportation().size())+
                ", Customers:"+Integer.toString(company.getCustomers().size())),0,5,TimeUnit.SECONDS);
    }
}
