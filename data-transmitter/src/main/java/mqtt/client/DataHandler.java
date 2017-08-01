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

package mqtt.client;

import websocket.server.IotDataServer;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Handles maps received through the subscription mechanism.
 * @since 1.0
 * @author Arthur Deschamps
 */
public class DataHandler {

    private String applicationId;
    private String publisherId;
    private String publisherAccountName;
    private IotDataServer wsServer;

    DataHandler(String applicationId, String publisherId, String publisherAccountName, IotDataServer wsServer) {
        this.applicationId = applicationId;
        this.publisherId = publisherId;
        this.publisherAccountName = publisherAccountName;
        this.wsServer = wsServer;
    }

    void handle(String topic, Map<String, Object> data) {
        if (data == null || topic == null)
            return;

        String[] segments = getSegments(topic);
        wsServer.send(segments, data);
    }

    /**
     * Retains only the topic itself (without for instance the application id) and splits it into multiple segments.
     * @param topic
     * A topic received from kapua.
     * @return
     * A list of strings representing the main and sub topics.
     */
    private String[] getSegments(String topic) {
        final String kapuaMeta = Stream.of(publisherAccountName, publisherId, applicationId).collect(Collectors.joining("/"))+"/";
        return topic.replace(kapuaMeta,"").split("/");
    }
}
