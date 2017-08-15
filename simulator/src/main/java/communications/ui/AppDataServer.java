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

package communications.ui;


import com.google.gson.Gson;
import communications.ui.utils.CustomersClusterFinder;
import company.company.Company;
import company.company.CompanyType;
import company.customer.Customer;
import company.transportation.Transportation;
import company.transportation.TransportationMode;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simulation.main.Parametrizer;
import simulation.main.VirtualTime;
import websocket.server.Response;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.*;

/**
 * Sends and receive non-iot data from the UI
 * @since 1.0
 * @author Arthur Deschamps
 */
public class AppDataServer extends org.java_websocket.server.WebSocketServer {

    private Company company;
    private Parametrizer parametrizer;
    private VirtualTime virtualTime;
    private CustomersClusterFinder customersClusterFinder;
    private final Gson gson = new Gson();

    private final static Logger logger = LoggerFactory.getLogger(AppDataServer.class);

    public AppDataServer(Parametrizer parametrizer, VirtualTime virtualTime, int port) {
        super(new InetSocketAddress("localhost",port));
        this.company = parametrizer.getCompany();
        this.virtualTime = virtualTime;
        this.parametrizer = parametrizer;
        this.customersClusterFinder = new CustomersClusterFinder(parametrizer);
        customersClusterFinder.build();
    }

    /**
     * Resolves a request and returns the asked result in Json format.
     * @param request
     * A request concerning non-iot data.
     * @return
     * The requested data in the form of a Json.
     */
    private Response resolve(String request) {
        String[] segments = request.split("/");
        Response result = new Response();
        Map<String, Object> data = new HashMap<>();

        // Frontend app expects to receive the same format for the request (e.g. topic/subtopic/...)
        result.setTopics(new String[]{request});

        // Now we actually parse the request be checking every acceptable case of request.
        // If the request is not one of the possible cases, then a null data will be responded to the client.
        if (segments.length == 3 && segments[0].equals("transportation") && segments[1].equals("type"))
            data.put("transportation-type", getTransportationMode(segments[2]));

        if (segments.length >= 2) {
            if (segments[0].equals("time") && segments[1].equals("now"))
                data.put("time", virtualTime.getCurrentDateTime());

            if (segments[0].equals("company")) {
                if (segments[1].equals("name")) data.put("name", company.getName());
                if (segments[1].equals("type")) data.put("company-type", company.getType().name());
                if (segments[1].equals("headquarters")) data.put("address", company.getHeadquarters());

                if (segments.length == 3 && segments[1].equals("customers")) {
                    if (segments[2].equals("all"))
                        data.put("customers", company.getCustomers().toArray());
                    if (segments[2].equals("clusters")) {
                        ExecutorService executorService = Executors.newSingleThreadExecutor();
                        Future<List<List<Customer>>> listFuture = executorService.submit(() -> customersClusterFinder.getClusters());
                        try {
                            data.put("customersAgglomerations", listFuture.get());
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            if (segments[0].equals("parametrizer") && segments[1].equals("timeFlow"))
                data.put("number", parametrizer.getTimeFlow());

            if (segments[0].equals("parametrizer") && segments[1].equals("dataSendingDelay"))
                data.put("number", parametrizer.getDataSendingDelay());
        }

        if (segments.length >= 1 && segments[0].equals("set")) {
            if (segments.length == 3) {
                if (segments[1].equals("companyName")) {
                    company.setName(segments[2]);
                    data.put("boolean",true);
                }
                if (segments[1].equals("companyType")) {
                    try {
                        CompanyType companyType = CompanyType.valueOf(segments[2].toUpperCase());
                        parametrizer.setCompanyType(companyType);
                        data.put("boolean", true);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        data.put("boolean", false);
                    }
                }
                if (segments[1].equals("timeFlow")) {
                    try {
                        final int timeFlow = Integer.valueOf(segments[2]);
                        if (timeFlow < 1 || timeFlow > Math.pow(10,6))
                            throw  new IllegalArgumentException("Time flow out of bounds");
                        parametrizer.setTimeFlow(timeFlow);
                        data.put("boolean", true);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        data.put("boolean", false);
                    }
                }
                if (segments[1].equals("dataSendingDelay")) {
                    try {
                        final int dataSendingDelay = Integer.valueOf(segments[2]);
                        if (dataSendingDelay < 1 || dataSendingDelay > 100)
                            throw new IllegalArgumentException("Data sending delay out of bounds");
                        parametrizer.setDataSendingDelay(dataSendingDelay);
                        data.put("boolean", true);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        data.put("boolean", false);
                    }
                }
            }
        }

        result.setData(data);
        return result;
    }

    private TransportationMode getTransportationMode(String transportationId) {
        Optional<Transportation> transportationOptional = company.getAllTransportation()
                .stream()
                .filter(transportation -> transportation.getId().equals(transportationId))
                .findFirst();

        // Required to have a copy of the enum to avoid concurrent access exception of gson conversion
        return transportationOptional.map(transportation ->
                TransportationMode.valueOf(transportation.getTransportationMode().name())).orElse(null);
    }

    private String toGson(Object obj) { return gson.toJson(obj); }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {

    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        webSocket.send(toGson(resolve(s)));
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        logger.error(e.getMessage());
        e.printStackTrace();
    }

    @Override
    public void onStart() {
        logger.info("Websocket for app data started on port " +this.getAddress().getPort()+"...");
    }
}
