package simulator;

import company.main.Company;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by Arthur Deschamps on 30.05.17.
 */
public class SupplyChainControlSimulator {

    private static Company company;
    private static Parametrizer parametrizer;
    private static EconomySimulatorRunner economySimulator;
    private static CompanySimulatorRunner companySimulator;
    private static final Logger logger = Logger.getLogger(SupplyChainControlSimulator.class.getName());

    public static void main(String[] args) {
        initDefault();
        runner();
    }

    private static void initDefault(){
        // Generate default company if user didn't choose any parameter
        company = new CompanyGenerator().generateDefault();
        // Generate default data
        //DefaultDataGenerator.generateDefaultDatabase();
        // Generate default parametrizer
        parametrizer = new Parametrizer(10000);

        economySimulator = new EconomySimulatorRunner();
        companySimulator = new CompanySimulatorRunner(company, economySimulator);
    }

    private static void runner() {
        // Number of threads: economy simulator, company simulator and info displaying
        final int threadsNbr = 2;

        // Convert time flow to delay
        long delay = (long) ((1/(double)parametrizer.getTimeFlow())* Math.pow(10,6));
        if (delay < 1)
            delay = 1;
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(threadsNbr);
        executor.scheduleWithFixedDelay(economySimulator,0,delay,TimeUnit.MICROSECONDS);
        executor.scheduleWithFixedDelay(companySimulator,0,delay,TimeUnit.MICROSECONDS);
        // Display data
        executor.scheduleWithFixedDelay(() -> logger.info("Growth: "+economySimulator.getGrowth()+", Demand: "+economySimulator.getDemand()
                +", Sector concurrency: "+economySimulator.getSectorConcurrency()),1,1,TimeUnit.SECONDS);
    }

}
