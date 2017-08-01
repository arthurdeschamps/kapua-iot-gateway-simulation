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

package simulation.simulators.company;

import company.company.Company;
import company.delivery.Delivery;
import company.order.Order;
import org.junit.Assert;
import org.junit.Test;
import simulation.generators.CompanyGenerator;
import simulation.generators.DataGenerator;

/**
 * @author Arthur Deschamps
 */

public class CompanySimulatorsTest {

    @Test
    public void testDeliveries() {
        Company company = CompanyGenerator.generateRandomCompany();
        DataGenerator dataGenerator = new DataGenerator(company);
        Order order = dataGenerator.generateRandomOrder().get();
        Delivery delivery = new Delivery(order,
                DataGenerator.generateRandomTransportation(),
                company.getHeadquarters(),
                order.getBuyer().getAddress());

        Assert.assertEquals(delivery.getCurrentLocation(), company.getHeadquarters().getCoordinates());
    }
}
