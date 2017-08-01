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
 * Description..
 *
 * @author Arthur Deschamps
 */
public class UpheavalSimulator extends AbstractEconomyComponentSimulator {

    public UpheavalSimulator(Economy economy) {
        super(economy);
    }

    @Override
    public void run() {
        simulateUpheavalLikelihood();
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
}
