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

/**
 * Simulates market demand
 * @since 1.0
 * @author Arthur Deschamps
 */
public class DemandSimulator extends AbstractEconomyComponentSimulator {

    public DemandSimulator(Economy economy) {
        super(economy);
    }

    @Override
    public void run() {
        simulateDemand();
    }

    private void simulateDemand() {
        /*
         Demand is a result of sector concurrency and economy growth but only reflects once in a while
         demand = demand + (growth/2 - concurrency)/100
          */
        if (random.nextInt(1000) == 0)
            economy.setDemand(Math.abs(economy.getDemand()+(economy.getGrowth()*0.1f-economy.getSectorConcurrency())/100));
    }
}
