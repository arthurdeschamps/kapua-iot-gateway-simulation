package simulator;

import sun.rmi.runtime.Log;

import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by Arthur Deschamps on 02.06.17.
 * This class gives a frame to the simulation. The economy determines the sales made by the simulated company
 */
public class Economy implements Runnable {

    // Economy global growth percentage
    private float growth;
    // Sector concurrency percentage
    private float sectorConcurrency;
    // Probability that an economic upheaval occurs
    private float upheavalLikelihood;
    // Sector demand percentage
    private float demand;
    // Time flow speediness in seconds per second
    private long timeFlow;

    private final Logger logger = Logger.getLogger(Economy.class.getName());

    private Random random = new Random();

    public Economy() {
        generateValues();
    }

    public Economy(long timeFlow) {
        generateValues();
        this.timeFlow = timeFlow;
    }

    public Economy(float growth, float sectorConcurrency, float upheavalLikelihood, float demand, long timeFlow) {
        this.growth = growth;
        this.sectorConcurrency = sectorConcurrency;
        this.upheavalLikelihood = upheavalLikelihood;
        this.demand = demand;
        this.timeFlow = timeFlow;
    }

    @Override
    public void run() {
        // The economy changes approximately every 1 second
        while (!Thread.currentThread().isInterrupted()) {
            try {
                simulateGrowth();
                simulateSectorConcurrency();
                simulateDemand();
                simulateUpheavalLikelihood();
                //displayAll();
                Thread.sleep(1000/timeFlow);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void simulateDemand() {
        //TODO
    }

    private void simulateUpheavalLikelihood() {
        /*
         Something terrible or incredible happens to the economy approximately once every month

         1 month = 30 days = 30 * 24 hours = 30 * 24 * 60 minutes = 30 * 24 * 60^2 seconds
          */
        int month = 30*24*60^2;
        if(random.nextInt(month) == 0) {
            // TODO:simulate groundbreaking event
            if (random.nextInt(2) == 0) {
                // Good event for our company
            } else {
                // Bad event for our company
            }
        }
    }

    private void simulateGrowth() {
        /*
         Growth increases or decreases by 0.0x on average once per minute with probability 0.5 each
         x takes random value between 0 and 9

         1 minute = 60 seconds
          */
        int minute = 60;
        if (random.nextInt(minute) == 0) {
            float economyGrowthIncrease = (float)Math.pow(-1,Math.floor(random.nextInt(2)))*random.nextInt(10)/100;
            float growth = this.getGrowth();
            if (growth+economyGrowthIncrease >= 0 && growth+economyGrowthIncrease <= 1)
                this.setGrowth(growth+economyGrowthIncrease);
        }
    }

    private void simulateSectorConcurrency() {
        /*
        Concurrency increases or decreases by 0.1 on average once a week with probability 0.5 each
        1 week = 7 days = 7*24 hours = 7*24*60 minutes = 7*24*60^2 seconds
         */
        int week = 7*24*60*2;
        float inc = 0;
        float concurrency = this.getSectorConcurrency();

        if (random.nextInt(week) == 0) {
            inc = 0.1f;
        }
        if (random.nextInt(week) == 0)
            inc = -0.1f;
        if (concurrency+inc >= 0 && concurrency+inc <= 1)
            this.setSectorConcurrency(concurrency+inc);
    }

    private void generateValues() {
        this.setGrowth(random.nextFloat());
        this.setDemand(random.nextFloat());
        this.setSectorConcurrency(random.nextFloat());
        this.setUpheavalLikelihood(0.001f);
        this.setTimeFlow(100);

        // Can't allow 0 for any percentage
        if (this.getGrowth() == 0 || this.getSectorConcurrency() == 0 || this.getDemand() == 0)
            generateValues();
    }

    public float getGrowth() {
        return growth;
    }

    public void setGrowth(float growth) {
        this.growth = growth;
    }

    public float getSectorConcurrency() {
        return sectorConcurrency;
    }

    public void setSectorConcurrency(float sectorConcurrency) {
        this.sectorConcurrency = sectorConcurrency;
    }

    public float getUpheavalLikelihood() {
        return upheavalLikelihood;
    }

    public void setUpheavalLikelihood(float upheavalLikelihood) {
        this.upheavalLikelihood = upheavalLikelihood;
    }

    public float getDemand() {
        return demand;
    }

    public void setDemand(float demand) {
        this.demand = demand;
    }

    public long getTimeFlow() {
        return timeFlow;
    }

    public void setTimeFlow(long timeFlow) {
        this.timeFlow = timeFlow;
    }
}
