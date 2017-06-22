package simulator.main;

import company.main.Company;
import simulator.generator.CompanyGenerator;
import simulator.runner.CompanySimulatorRunner;
import simulator.runner.DeliveryMovementSimulatorRunner;
import simulator.runner.EconomySimulatorRunner;

import java.util.concurrent.ExecutionException;
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
    private  Parametrizer parametrizer;
    private  EconomySimulatorRunner economySimulator;
    private  CompanySimulatorRunner companySimulator;
    private DeliveryMovementSimulatorRunner productMovementSimulator;
    private  final Logger logger = Logger.getLogger(SupplyChainControlSimulator.class.getName());

    public SupplyChainControlSimulator() {
        initDefault();
        runner();
    }

    /**
     * Initialize default data (company, parametrizer, etc)
     */
    private  void initDefault() {
        // Generate default company if user didn't choose any parameter
        company = new CompanyGenerator().generateDefaultCompany();

        // Generate default parametrizer
        parametrizer = new Parametrizer(10000);

        economySimulator = new EconomySimulatorRunner();
        companySimulator = new CompanySimulatorRunner(company, economySimulator);
        productMovementSimulator = new DeliveryMovementSimulatorRunner(companySimulator);
    }

    private  void runner() {
        // Number of threads: economy simulator, company simulator and info displaying
        final int threadsNbr = 4;

        // Convert time flow to delay
        long delay = (long) ((1/(double)parametrizer.getTimeFlow())* Math.pow(10,6));
        if (delay < 1)
            delay = 1;

        try {
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(threadsNbr);
            // EconomySimulator and CompanySimulator are made to simulate 1 second
            executor.scheduleWithFixedDelay(economySimulator,0,delay,TimeUnit.MICROSECONDS);
            executor.scheduleWithFixedDelay(companySimulator,0,delay,TimeUnit.MICROSECONDS);
            // ProductMovementSimulator is made to simulate 1 hour
            executor.scheduleWithFixedDelay(productMovementSimulator,0,delay*3600,TimeUnit.MICROSECONDS);
            executor.scheduleWithFixedDelay(() -> company.getDeliveries().stream().findFirst().ifPresent(delivery ->
                    logger.info(Double.toString(delivery.getDistanceFromDestination()))),1,5,TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Display information on economics (growth, demand, etc).
     * @param executor
     * ScheduledExecutorService to attach the thread to.
     */
    public void displayEconomicalInfo(ScheduledExecutorService executor) {
        executor.scheduleWithFixedDelay(() -> logger.info("Growth: "+economySimulator.getGrowth()+", Demand: "+economySimulator.getDemand()
                +", Sector concurrency: "+economySimulator.getSectorConcurrency()),1,5,TimeUnit.SECONDS);
    }

    /**
     * Display information on company (products, orders, etc).
     * @param executor
     * ScheduledExecutorService to attach the thread to.
     */
    public void displayCompanyInfo(ScheduledExecutorService executor) {
        executor.scheduleWithFixedDelay(() -> logger.info("Products: "+Integer.toString(company.getProducts().size())+", Types: "+
                Integer.toString(company.getProductTypes().size())+
                ", Orders: "+Integer.toString(company.getOrders().size())+", Deliveries: "+Integer.toString(company.getDeliveries().size())
                +", Transportation: "+Integer.toString(company.getAllTransportation().size())+
                ", Customers:"+Integer.toString(company.getCustomers().size())),0,5,TimeUnit.SECONDS);
    }

    public  Company getCompany() {
        return company;
    }
}
