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
 * Simulates sector concurrency.
 * @since 1.0
 * @author Arthur Deschamps
 */
public class SectorConcurrencySimulator extends AbstractEconomyComponentSimulator {

    public SectorConcurrencySimulator(Economy economy) {
        super(economy);
    }

    @Override
    public void run() {
        simulateSectorConcurrency();
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
