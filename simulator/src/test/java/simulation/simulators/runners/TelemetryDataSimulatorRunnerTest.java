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

import company.address.Coordinates;
import company.company.Company;
import company.delivery.Delivery;
import company.delivery.DeliveryStatus;
import company.order.Order;
import company.transportation.Transportation;
import company.transportation.TransportationHealthState;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simulation.generators.CompanyGenerator;
import simulation.generators.DataGenerator;

import java.util.Optional;

/**
 * Test for TelemetryDataSimulatorRunner class
 *
 * @author Arthur Deschamps
 */
public class TelemetryDataSimulatorRunnerTest {

    private static TelemetryDataSimulatorRunner telemetryDataSimulatorRunner;
    private static Company company;

    @BeforeClass
    public static void setUp() {
        company = CompanyGenerator.generateLocalCompany();
        telemetryDataSimulatorRunner = new TelemetryDataSimulatorRunner(company);
    }

    @Test
    public void testMoveDelivery() {

        Delivery delivery = addDelivery();

        // Start shipping
        company.startDeliveryShipping(delivery);
        Assert.assertEquals(DeliveryStatus.TRANSIT,delivery.getDeliveryState());

        // Retain initial position
        Coordinates coordinatesBefore, coordinatesAfter, destination;

        destination = delivery.getDestination().getCoordinates();

        coordinatesBefore = delivery.getCurrentLocation();
        Logger logger = LoggerFactory.getLogger(TelemetryDataSimulatorRunnerTest.class);
        // Test that the delivery arrives at some point
        for (int i = 0; i < 1000; i++)
            telemetryDataSimulatorRunner.run();

        coordinatesAfter = delivery.getCurrentLocation();

        Assert.assertNotEquals(coordinatesBefore, coordinatesAfter);
        Assert.assertEquals(0,
                Coordinates.calculateDistance(coordinatesAfter,destination),
                50);
    }

    @Test
    public void testTransportationDegradation() {
        Transportation transportation = DataGenerator.generateRandomTransportation();
        TransportationHealthState healthBefore = transportation.getHealthState();
        company.newTransportation(transportation);
        Assert.assertTrue(transportation.isAvailable());
        for (int i = 0; i < Math.pow(10, 8); i++) {
            telemetryDataSimulatorRunner.run();
            if (!transportation.getHealthState().equals(healthBefore))
                return;
        }
        Assert.fail("Health state never changed.");
    }

    @Test
    public void testDeliveryShipping() {
        Delivery delivery = addDelivery();
        Assert.assertEquals(DeliveryStatus.WAREHOUSE,delivery.getDeliveryState());
        for (int i = 0; i < Math.pow(10, 8); i++) {
            telemetryDataSimulatorRunner.run();
            if (delivery.getDeliveryState().equals(DeliveryStatus.TRANSIT))
                return;
            // Precaution if delivery gets cancelled
            if (delivery.getDeliveryState().equals(DeliveryStatus.CANCELLED))
                delivery.setDeliveryState(DeliveryStatus.WAREHOUSE);
        }
        Assert.fail("Delivery state never went from warehouse to transit");
    }

    @Test
    public void testDeliveryCancellation() {
        Delivery delivery = addDelivery();
        Assert.assertEquals(DeliveryStatus.WAREHOUSE,delivery.getDeliveryState());
        for (int i = 0; i < Math.pow(10, 8); i++) {
            telemetryDataSimulatorRunner.run();
            if (delivery.getDeliveryState().equals(DeliveryStatus.CANCELLED))
                return;
            if (!delivery.getDeliveryState().equals(DeliveryStatus.WAREHOUSE))
                delivery.setDeliveryState(DeliveryStatus.WAREHOUSE);
        }
        Assert.fail("Delivery was never cancelled.");
    }

    private Delivery addDelivery() {
        // Makes sure that company has a delivery
        DataGenerator dataGenerator = new DataGenerator(company);
        Optional<Order> order = dataGenerator.generateRandomOrder();
        Assert.assertTrue(order.isPresent());
        company.newOrder(order.get());
        Optional<Delivery> delivery = dataGenerator.generateRandomDelivery();
        Assert.assertTrue(delivery.isPresent());
        company.newDelivery(delivery.get());

        Assert.assertTrue(company.getDeliveries().contains(delivery.get()));
        return delivery.get();
    }

}