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

import java.util.Random;

/**
 * Model for an economy component simulator.
 * @since 1.0
 * @author Arthur Deschamps
 */
public abstract class AbstractEconomyComponentSimulator implements Runnable {

    protected Economy economy;
    protected final Random random = new Random();
    private final ProbabilityUtils probabilityUtils = new ProbabilityUtils();

    public AbstractEconomyComponentSimulator(Economy economy) {
        this.economy = economy;
    }

}
