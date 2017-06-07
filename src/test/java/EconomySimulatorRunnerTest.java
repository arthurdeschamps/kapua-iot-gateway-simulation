import org.junit.Test;
import simulator.EconomySimulatorRunner;

import java.util.logging.Logger;

/**
 * Created by Arthur Deschamps on 05.06.17.
 * This test is supposed to verify if the law of large numbers is verified for each economy parameter
 */
public class EconomySimulatorRunnerTest {

    @Test
    public void runEconomy() throws InterruptedException {
        EconomySimulatorRunner economy = new EconomySimulatorRunner();
        new Thread(economy).start();

        Thread.sleep(1000);

        float initialGrowth = economy.getGrowth();
        float initialSectorConcurrency = economy.getSectorConcurrency();
        while ((initialGrowth == economy.getGrowth()) || (initialSectorConcurrency == economy.getSectorConcurrency())) {
            Thread.yield();
        }

        Logger.getGlobal().info("Initial/new values: "+
                "Growth: "+initialGrowth+"/"+economy.getGrowth()+", "+
                "Concurrency: "+initialSectorConcurrency+"/"+economy.getSectorConcurrency()
        );
    }
}
