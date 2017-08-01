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

import mqtt.client.MqttSubscriptionsManager;
import org.slf4j.LoggerFactory;
import websocket.server.IotDataServer;

/**
 * @since 1.0
 * @author Arthur Deschamps
 */

public class Main {

    public static void main(String[] args) {
        LoggerFactory.getLogger("data-transmitter").info("Opening websocket...");
        final int wsPort = 8054;
        IotDataServer wsServer = new IotDataServer(wsPort);
        wsServer.start();

        final int mqttPort = 1883;
        final String host = "localhost";
        new MqttSubscriptionsManager(host,mqttPort, wsServer).startListening();
    }
}
