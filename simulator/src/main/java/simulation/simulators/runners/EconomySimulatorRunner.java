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

package simulation.simulators.runners;

import economy.Economy;
import simulation.simulators.economy.*;

/**
 * Simulates economy.
 * @author Arthur Deschamps
 * @since 1.0
 */
public class EconomySimulatorRunner extends AbstractRunner<AbstractEconomyComponentSimulator> {

    public EconomySimulatorRunner(Economy economy) {
        super(new AbstractEconomyComponentSimulator[] {
                new DemandSimulator(economy),
                new GrowthSimulator(economy),
                new SectorConcurrencySimulator(economy),
                new UpheavalSimulator(economy)
        });
    }

}
