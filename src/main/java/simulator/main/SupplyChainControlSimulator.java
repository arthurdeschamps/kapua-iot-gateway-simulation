package simulator.main;

import company.main.Company;
import simulator.runner.EconomySimulatorRunner;
import simulator.generator.CompanyGenerator;
import simulator.runner.CompanySimulatorRunner;
import simulator.runner.DeliveryMovementSimulatorRunner;

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

    private  void initDefault() {
        // Generate default company if user didn't choose any parameter
        company = CompanyGenerator.getInstance().generateDefaultCompany();

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
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(threadsNbr);
        executor.scheduleWithFixedDelay(economySimulator,0,delay,TimeUnit.MICROSECONDS);
        executor.scheduleWithFixedDelay(companySimulator,0,delay,TimeUnit.MICROSECONDS);
        executor.scheduleWithFixedDelay(productMovementSimulator,0,delay,TimeUnit.MICROSECONDS);
        // Display data
        executor.scheduleWithFixedDelay(() -> logger.info("Growth: "+economySimulator.getGrowth()+", Demand: "+economySimulator.getDemand()
                +", Sector concurrency: "+economySimulator.getSectorConcurrency()),1,5,TimeUnit.SECONDS);
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
