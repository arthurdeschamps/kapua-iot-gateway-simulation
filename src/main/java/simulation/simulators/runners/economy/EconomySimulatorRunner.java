package simulation.simulators.runners.economy;

import economy.Economy;
import simulation.util.ProbabilityUtils;

import java.util.Random;

/**
 * Created by Arthur Deschamps on 02.06.17.
 * This class gives a frame to the simulation. The economy determines the sales made by the simulated company.
 * 1 call to run is equivalent to 1 second in "real" time
 */
public class EconomySimulatorRunner implements Runnable {

    private Random random = new Random();
    private final ProbabilityUtils probabilityUtils = new ProbabilityUtils();
    private Economy economy;

    public EconomySimulatorRunner(Economy economy) {
        this.economy = economy;
    }

    @Override
    public void run() {
        try {
            simulateGrowth();
            simulateSectorConcurrency();
            simulateDemand();
            simulateUpheavalLikelihood();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void simulateDemand() {
        /*
         Demand is a result of sector concurrency and economy growth but only reflects once in a while
         demand = demand + (growth/2 - concurrency)/100
          */
        if (random.nextInt(1000) == 0)
            economy.setDemand(Math.abs(economy.getDemand()+(economy.getGrowth()*0.1f-economy.getSectorConcurrency())/100));
    }

    private void simulateUpheavalLikelihood() {
        /*
         Something terrible or incredible happens to the economy
         Probability depends on the parameter upheavallikelihood
          */
        if(random.nextInt(Math.round(1/economy.getUpheavalLikelihood())) == 0) {
            if (random.nextInt(2) == 0) {
                // Good event for our company
                economy.setGrowth(economy.getGrowth()+3f);
            } else {
                // Bad event for our company
                economy.setGrowth(economy.getGrowth()-3f);
            }
        }
    }

    /**
     * Simulates economy total growth
     */
    private void simulateGrowth() {
        /*
         Growth increases or decreases by 0.00x on average once per hour with probability 0.5 each
          */
        if (probabilityUtils.event(1, ProbabilityUtils.TimeUnit.HOUR))
            if (Math.random() > 0.5d) {
                // Totally random increase factor
                float economyGrowthIncrease = (float) (Math.pow(-1, random.nextInt(2)) * random.nextInt(10) / 1000);
                //Logger.getGlobal().info("economyGrowthIncrease = "+economyGrowthIncrease);
                // Factor based on growth magnitude: when the growth gets high (positive or negative), the curve inverts
                float magnitudeCorrecter = 0;
                if (probabilityUtils.event(3, ProbabilityUtils.TimeUnit.WEEK))
                    magnitudeCorrecter = -economy.getGrowth()*(60/100);
                //Logger.getGlobal().info("magnitudeCorrecter = "+magnitudeCorrecter);
                economy.setGrowth(economy.getGrowth() + economyGrowthIncrease + magnitudeCorrecter);
            }
    }

    private void simulateSectorConcurrency() {
        /*
        Concurrency increases or decreases by 0.001 on average once a day with probability 0.5 each
        1 day = 24 hours = 24*60 seconds
         */
        final int day = 24*60;
        float inc = 0;
        if (random.nextInt(day) == 0)
            inc = 0.1f;
        if (random.nextInt(day) == 0)
            inc = -0.1f;
        economy.setSectorConcurrency(economy.getSectorConcurrency()+inc);
    }

}
