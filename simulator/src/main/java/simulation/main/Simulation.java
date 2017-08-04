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

package simulation.main;

import communications.kapua.KapuaClient;
import communications.ui.AppDataServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simulation.simulators.SupplyChainControlSimulator;

/**
 * Main simulation. Runs economy, company and delivery transport simulations and sends data to kapua.
 * @author Arthur Deschamps
 * @since 1.0
 */
public class Simulation {

    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(Simulation.class);

        logger.info("Creating parametrizer...");
        // Uncomment the two lines below and modify the values as you wish to parametrize the simulation
        // Parametrizer parametrizer = new Parametrizer(100, 3, false, 0,
        //         CompanyGenerator.generateInternationalCompany(), false, null);

        // Comment the line below if you don't want the default parameters for the simulation
        Parametrizer parametrizer = new Parametrizer();

        VirtualTime virtualTime = new VirtualTime(parametrizer);

        // Or use the default parametrizer and set the things you want:
        parametrizer.setDataSendingDelay(2);
        parametrizer.setTimeFlow(3600);
        parametrizer.setDisplayMetrics(false);

        // Starts the simulation
        logger.info("Starting simulators...");
        new SupplyChainControlSimulator(parametrizer).start();

        // Start sending data and subscribing
        logger.info("Initializing communications with Kapua...");
        new KapuaClient(parametrizer.getCompany(),parametrizer).startCommunications();

        logger.info("Initializing communications with frontend app...");
        final int wsPort = 8055;
        new AppDataServer(parametrizer, virtualTime, wsPort).start();
    }

}
