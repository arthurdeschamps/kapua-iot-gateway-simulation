import org.junit.Test;
import simulator.Economy;

import java.util.logging.Logger;

/**
 * Created by Arthur Deschamps on 05.06.17.
 */
public class EconomyTest {

    @Test
    public void runEconomy() throws InterruptedException {
        Economy economy = new Economy();
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
