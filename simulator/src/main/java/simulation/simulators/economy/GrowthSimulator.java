/*
 * ******************************************************************************
 *  * Copyright (c) 2017 Arthur Deschamps
 *  *
 *  * All rights reserved. This program and the accompanying materials
 *  * are made available under the terms of the Eclipse Public License v1.0
 *  * which accompanies this distribution, and is available at
 *  * http://www.eclipse.org/legal/epl-v10.html
 *  *
 *  * Contributors:
 *  *     Arthur Deschamps
 *  ******************************************************************************
 */

package simulation.simulators.economy;

import economy.Economy;
import simulation.util.ProbabilityUtils;

/**
 * Simulates economical growth.
 * @since 1.0
 * @author Arthur Deschamps
 */
public class GrowthSimulator extends AbstractEconomyComponentSimulator {

    private final ProbabilityUtils probabilityUtils = new ProbabilityUtils();

    public GrowthSimulator(Economy economy) {
        super(economy);
    }

    @Override
    public void run() {
        simulateGrowth();
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
                // Factor based on growth magnitude: when the growth gets high (positive or negative), the curve inverts
                float magnitudeCorrecter = 0;
                if (probabilityUtils.event(3, ProbabilityUtils.TimeUnit.WEEK))
                    magnitudeCorrecter = -economy.getGrowth()*(60/100);
                economy.setGrowth(economy.getGrowth() + economyGrowthIncrease + magnitudeCorrecter);
            }
    }
}
