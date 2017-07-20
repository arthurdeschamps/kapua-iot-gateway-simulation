package economy;

/**
 * Describes a global economy.
 * @since 1.0
 * @author Arthur Deschamps
 */
public class Economy {

    // EconomySimulatorRunner global growth. Can be negative and can be greater than 1 in absolute value.
    private float growth;
    // Sector concurrency percentage
    private float sectorConcurrency;
    // Probability that an economic upheaval occurs
    private float upheavalLikelihood;
    // Sector demand percentage
    private float demand;

    public Economy(float growth, float sectorConcurrency, float upheavalLikelihood, float demand) {
        this.growth = growth;
        this.sectorConcurrency = sectorConcurrency;
        this.upheavalLikelihood = upheavalLikelihood;
        this.demand = demand;
    }

    public Economy() {
        this.setGrowth(0);
        this.setDemand(0.5f);
        this.setSectorConcurrency(0.5f);
        this.setUpheavalLikelihood(0.000001f);
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

