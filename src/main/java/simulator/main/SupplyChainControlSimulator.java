package simulator.main;

import company.main.Company;
import economy.Economy;
import simulator.runner.CompanySimulatorRunner;
import simulator.runner.DeliveryMovementSimulatorRunner;
import simulator.runner.EconomySimulatorRunner;
import simulator.util.Parametrizer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

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
    private DeliveryMovementSimulatorRunner movementSimulator;

    private long delay;

    private  final Logger logger = Logger.getLogger(SupplyChainControlSimulator.class.getName());

    public SupplyChainControlSimulator(Company company, Economy economy, Parametrizer parametrizer) {
        this.company = company;
        this.economy = economy;
        this.parametrizer = parametrizer;

        economySimulator = new EconomySimulatorRunner(economy);
        companySimulator = new CompanySimulatorRunner(company,economy);
        movementSimulator = new DeliveryMovementSimulatorRunner(company);

        this.delay = convertToDelay(parametrizer.getTimeFlow());
    }

    /**
     * Starts simulation with only the strict minimum, that is the economy, company and delivery movement simulators.
     */
    public void start() {

        // Number of threads: economy simulator, company simulator and info displaying
        final int threadsNbr = 4;

        try {
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(threadsNbr);

            // EconomySimulator and CompanySimulator are made to simulate 1 second per execution
            executor.scheduleWithFixedDelay(economySimulator,0,delay,TimeUnit.MICROSECONDS);
            executor.scheduleWithFixedDelay(companySimulator,0,delay,TimeUnit.MICROSECONDS);

            // ProductMovementSimulator is made to simulate 1 hour per execution
            executor.scheduleWithFixedDelay(movementSimulator,0,delay*3600,TimeUnit.MICROSECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Display information on economics (growth, demand, etc).
     * @param executor
     * ScheduledExecutorService to attach the thread to.
     */
    public void displayEconomicalData(ScheduledExecutorService executor) {
        executor.scheduleWithFixedDelay(() -> logger.info("Growth: "+economy.getGrowth()+", Demand: "+economy.getDemand()
                +", Sector concurrency: "+economy.getSectorConcurrency()),1,5,TimeUnit.SECONDS);
    }

    /**
     * Display information on company (products, orders, etc).
     * @param executor
     * ScheduledExecutorService to attach the thread to.
     */
    public void displayCompanyData(ScheduledExecutorService executor) {
        executor.scheduleWithFixedDelay(() -> logger.info("Products: "+Integer.toString(company.getProducts().size())+", Types: "+
                Integer.toString(company.getProductTypes().size())+
                ", Orders: "+Integer.toString(company.getOrders().size())+", Deliveries: "+Integer.toString(company.getDeliveries().size())
                +", Transportation: "+Integer.toString(company.getAllTransportation().size())+
                ", Customers:"+Integer.toString(company.getCustomers().size())),0,5,TimeUnit.SECONDS);
    }

    /**
     * Convert time flow (speed of time flow for the simulation) to delay for the runnables.
     * @param timeFlow
     * Speed of time flow decided by the user. For instance, if timeFlow is 100 then 1 second in simulation = 100 seconds
     * in real time.
     * @return
     * timeFlow converted to a delay for the runnable objects. This delay is used to indicate the frequency at which
     * an Executor shall run our simulators.
     */
    private long convertToDelay(long timeFlow) {
        // Convert time flow to delay
        long delay = (long) ((1/(double)parametrizer.getTimeFlow())* Math.pow(10,6));
        if (delay < 1)
            delay = 1;
        return delay;
    }
}
