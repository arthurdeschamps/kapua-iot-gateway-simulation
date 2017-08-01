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

package communications.kapua;

import company.company.Company;
import org.eclipse.kapua.gateway.client.Application;
import org.eclipse.kapua.gateway.client.mqtt.paho.PahoClient;
import org.eclipse.kapua.gateway.client.profile.kura.KuraMqttProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simulation.main.Parametrizer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.eclipse.kapua.gateway.client.Credentials.userAndPassword;
import static org.eclipse.kapua.gateway.client.Transport.waitForConnection;

/**
 * Interface and communicate with Kapua
 * @author Arthur Deschamps
 * @since 1.0
 */
public class KapuaClient {

    private Company company;
    private Parametrizer parametrizer;
    private org.eclipse.kapua.gateway.client.Client client;
    private Application application;

    private int communicationsDelay;
    private final int port = 1883;
    private final String host = "localhost";
    private final String accountName = "kapua-sys";
    private final String applicationId = "kapua-iot-gateway-simulation-scm";
    private final String clientId = "supply-chain-control-simulation";

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private static final Logger logger = LoggerFactory.getLogger(KapuaClient.class);

    public KapuaClient(Company company, Parametrizer parametrizer) {
        this.parametrizer = parametrizer;
        this.company = company;
        this.communicationsDelay = parametrizer.getDataSendingDelay();

        try {
            client = KuraMqttProfile.newProfile(PahoClient.Builder::new)
                    .accountName(accountName)
                    .clientId(clientId)
                    .brokerUrl("tcp://"+host+":"+Integer.toString(port))
                    .credentials(userAndPassword(accountName, "kapua-password"))
                    .build();

        } catch (Exception e) {
           logger.error(e.getMessage());
        }

        application = client.buildApplication(applicationId).build();

    }

    /**
     * Starts sending data to kapua.
    **/
    public void startCommunications() {
        try {
            // Wait for connection
            waitForConnection(application.transport());
            // Start sending data
            ScheduledFuture<?> send = executor.scheduleWithFixedDelay(
                            new DataSenderRunner(company,application),
                            0,
                            communicationsDelay,
                            TimeUnit.SECONDS
            );
            checkParametrizer(send);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void checkParametrizer(ScheduledFuture<?> send) {
        executor.schedule(() -> {
            if (communicationsDelay != parametrizer.getDataSendingDelay()) {
                logger.info("Data sending delay changed");
                communicationsDelay = parametrizer.getDataSendingDelay();
                send.cancel(false);

                startCommunications();
            } else {
                checkParametrizer(send);
            }
        }, 5, TimeUnit.SECONDS);
    }

}
