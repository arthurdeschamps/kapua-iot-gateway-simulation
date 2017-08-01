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

import company.company.Company;
import simulation.simulators.telemetry.AbstractTelemetryComponentSimulator;
import simulation.simulators.telemetry.DeliveryMovementSimulator;
import simulation.simulators.telemetry.DeliveryStatusSimulator;
import simulation.simulators.telemetry.TransportationHealthStateSimulator;

/**
 * Simulates different events related to telemetry data.
 * @author Arthur Deschamps
 * @since 1.0
 */
public class TelemetryDataSimulatorRunner extends AbstractRunner<AbstractTelemetryComponentSimulator> {

    public TelemetryDataSimulatorRunner(Company company) {
        super(new AbstractTelemetryComponentSimulator[] {
                new DeliveryMovementSimulator(company),
                new DeliveryStatusSimulator(company),
                new TransportationHealthStateSimulator(company)
        });
    }
}
