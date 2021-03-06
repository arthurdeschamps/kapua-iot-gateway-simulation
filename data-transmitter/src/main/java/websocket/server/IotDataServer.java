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

package websocket.server;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Map;

/**
 * Communicates with the frontend app
 * @since 1.0
 * @author Arthur Deschamps
 */
public class IotDataServer extends WebSocketServer {

    private final Logger logger = LoggerFactory.getLogger(IotDataServer.class);
    private final Sender sender = new Sender();

    public IotDataServer(int port) {
        super(new InetSocketAddress(port));
    }

    public void send(String[] segments, Map<String, Object> data) {
        sender.send(segments,data);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        sender.addSubscriber(webSocket);
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        sender.removeSubscriber(webSocket);
    }

    @Override
    public void onMessage(WebSocket webSocket, String jsonRequest) {
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        logger.error(e.getMessage());
        e.printStackTrace();
    }

    @Override
    public void onStart() {
        logger.info("Websocket for IoT data started on port " +this.getAddress().getPort()+"...");
    }
}
