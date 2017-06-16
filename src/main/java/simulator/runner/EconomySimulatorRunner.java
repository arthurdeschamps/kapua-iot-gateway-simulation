package simulator.runner;

import java.util.Random;

/**
 * Created by Arthur Deschamps on 02.06.17.
 * This class gives a frame to the simulation. The economy determines the sales made by the simulated company.
 * 1 call to run is equivalent to 1 second in "real" time
 */
public class EconomySimulatorRunner implements Runnable {

    // EconomySimulatorRunner global growth. Can be negative and can be greater than 1 in absolute value.
    private float growth;
    // Sector concurrency percentage
    private float sectorConcurrency;
    // Probability that an economic upheaval occurs
    private float upheavalLikelihood;
    // Sector demand percentage
    private float demand;

    private Random random = new Random();
    private final ProbabilitySimulator probabilitySimulator = new ProbabilitySimulator();

    public EconomySimulatorRunner() {
        generateValues();
    }

    public EconomySimulatorRunner(float growth, float sectorConcurrency, float upheavalLikelihood, float demand) {
        this.growth = growth;
        this.sectorConcurrency = sectorConcurrency;
        this.upheavalLikelihood = upheavalLikelihood;
        this.demand = demand;
    }

    private void generateValues() {
        this.setGrowth(0);
        this.setDemand(0.5f);
        this.setSectorConcurrency(0.5f);
        this.setUpheavalLikelihood(0.000001f);
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
            this.setDemand(this.getDemand()+(this.getGrowth()*0.1f-this.getSectorConcurrency())/100);
    }

    private void simulateUpheavalLikelihood() {
        /*
         Something terrible or incredible happens to the economy
         Probability depends on the parameter upheavallikelihood
          */
        if(random.nextInt(Math.round(1/this.getUpheavalLikelihood())) == 0) {
            if (random.nextInt(2) == 0) {
                // Good event for our company
                this.setGrowth(this.getGrowth()+3f);
            } else {
                // Bad event for our company
                this.setGrowth(this.getGrowth()-3f);
            }
        }
    }

    /**
     * Simulates economy total growth
     */
    private void simulateGrowth() {
        /*
         Growth increases or decreases by 0.0x on average once per hour with probability 0.5 each
          */
        if (probabilitySimulator.event(1, ProbabilitySimulator.TimeUnit.HOUR))
            if (Math.random() > 0.5d) {
                // Totally random increase factor
                float economyGrowthIncrease = (float) (Math.pow(-1, random.nextInt(2)) * random.nextInt(10) / 100);
                //Logger.getGlobal().info("economyGrowthIncrease = "+economyGrowthIncrease);
                // Factor based on growth magnitude: when the growth gets high (positive or negative), the curve inverts
                float magnitudeCorrecter = 0;
                if (random.nextInt((int) (Math.abs((500 - Math.pow(this.getGrowth(), 2))) + 1)) == 0)
                    magnitudeCorrecter = Math.signum(this.getGrowth()) * Math.abs(this.getGrowth());
                //Logger.getGlobal().info("magnitudeCorrecter = "+magnitudeCorrecter);
                this.setGrowth(this.getGrowth() + economyGrowthIncrease + magnitudeCorrecter);
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
        this.setSectorConcurrency(this.getSectorConcurrency()+inc);
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
        if (sectorConcurrency > 1) {
            this.sectorConcurrency = 1;
        } else if (sectorConcurrency < 0) {
            this.sectorConcurrency = 0;
        } else {
            this.sectorConcurrency = sectorConcurrency;
        }
    }

    public float getUpheavalLikelihood() {
        return upheavalLikelihood;
    }

    public void setUpheavalLikelihood(float upheavalLikelihood) {
        if (upheavalLikelihood > 1) {
            this.upheavalLikelihood = 1;
        } else if (upheavalLikelihood < 0) {
            this.upheavalLikelihood = 0;
        } else {
            this.upheavalLikelihood = upheavalLikelihood;
        }
    }

    public float getDemand() {
        return demand;
    }

    public void setDemand(float demand) {
        if (demand > 1) {
            this.demand = 1;
        } else if (demand < 0) {
            this.demand = 0;
        } else {
            this.demand = demand;
        }
    }
}
