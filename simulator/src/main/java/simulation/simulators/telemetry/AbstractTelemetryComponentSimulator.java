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

package simulation.simulators.telemetry;

import company.company.Company;
import simulation.util.ProbabilityUtils;

/**
 * Model for a telemetry simulator.
 * @since 1.0
 * @author Arthur Deschamps
 */
public abstract class AbstractTelemetryComponentSimulator implements Runnable {

    protected Company company;
    protected final ProbabilityUtils probabilityUtils = new ProbabilityUtils();

    AbstractTelemetryComponentSimulator(Company company) {
        this.company = company;
    }
}
