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

package communications.ui.utils;

import company.address.Address;
import company.address.Coordinates;
import company.company.CompanyType;
import company.customer.Customer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simulation.main.Parametrizer;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

/**
 * Description..
 *
 * @author Arthur Deschamps
 */
public class CustomersClusterFinderTest {

    private Parametrizer parametrizer;
    private CustomersClusterFinder dbscanner;
    private List<Customer> customers;
    private final Random random = new Random();

    private final Logger logger = LoggerFactory.getLogger(CustomersClusterFinderTest.class);

    @Before
    public void setUp() throws Exception {
        parametrizer = new Parametrizer(null);
        parametrizer.getCompany().setType(CompanyType.INTERNATIONAL);
        dbscanner = new CustomersClusterFinder(parametrizer);
        customers = new ArrayList<>();
        float longitude;
        float latitude;
        for (int i = 0; i < 10000; i++) {
            longitude = random.nextFloat()*400-200;
            latitude = random.nextFloat()*400-200;
            customers.add(new Customer(Integer.toString(i),"",new Address("","","","","",
                    new Coordinates(latitude, longitude)),"",""));
        }
        parametrizer.getCompany().getCustomerStore().setStorage(new HashSet<>(customers));
        Assert.assertEquals(parametrizer.getCompany().getCustomers().size(), 10000);
    }


    @Test(timeout = 120000)
    public void testPerformances() {
        long start = new Date().getTime();
        List<List<Customer>> clusters = dbscanner.getClusters();
        logger.info("Number of clusters found: "+clusters.size());
        long stop = new Date().getTime();
        logger.info("Time elapsed in seconds: "+((stop-start)/1000));
    }

}