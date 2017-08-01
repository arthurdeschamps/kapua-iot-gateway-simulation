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
import company.delivery.Delivery;
import company.delivery.DeliveryStatus;

/**
 * Simulates product movement during deliveries.
 * @since 1.0
 * @author Arthur Deschamps
 */
public class DeliveryMovementSimulator extends AbstractTelemetryComponentSimulator  {

    public DeliveryMovementSimulator(Company company) {
        super(company);
    }

    @Override
    public void run() {
        company.getDeliveries().forEach(delivery -> {
            if (delivery.getDeliveryState().equals(DeliveryStatus.TRANSIT))
                moveDelivery(delivery);
        });
    }

    /**
     * Simulates delivery movement during one hour. A delivery moves toward the direction of its destination.
     * @param delivery
     * Delivery to be moved.
     */
    private void moveDelivery(Delivery delivery) {
        // Get speed in km/s
        final float speed = delivery.getTransporter().getActualSpeed();

        // We simulate 1 hour
        final float distance = speed*3600;

        // Move in the distance from destination minimising direction
        delivery.setCurrentLocation(delivery.minimizeDistanceFromDestination(distance));
    }

}
