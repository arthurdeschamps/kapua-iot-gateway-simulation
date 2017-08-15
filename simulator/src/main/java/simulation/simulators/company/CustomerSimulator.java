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
import economy.Economy;
import simulation.generators.DataGenerator;
import simulation.util.ProbabilityUtils;

/**
 * Simulates everything related to customers
 * @since 1.0
 * @author Arthur Deschamps
 */
public class CustomerSimulator extends AbstractCompanyComponentSimulator {

    private DataGenerator dataGenerator;

    public CustomerSimulator(Company company, Economy economy) {
        super(company, economy);
        this.dataGenerator = new DataGenerator(company);
    }

    @Override
    public void run() {
        simulateCustomersBehavior();
    }

    /**
     * Simulates acquisition of new customers of lost of customers for the company.
     * @since 1.0
     */
    private void simulateCustomersBehavior() {
        if (economy.getGrowth() > 0) {
            // If economy growth is high, there is a high chance of getting a new customer
            if (probabilityUtils.event(Math.abs(economy.getGrowth()*2), ProbabilityUtils.TimeUnit.DAY))
                company.newCustomer(dataGenerator.generateRandomCustomer());
            // It can still lose customers sometimes
            if (probabilityUtils.event(Math.abs(economy.getGrowth()), ProbabilityUtils.TimeUnit.DAY))
                company.deleteRandomCustomer();
        } else {
            // If economy growth is negative, there is a chance that our company might lose customers
            if (probabilityUtils.event(Math.abs(economy.getGrowth()), ProbabilityUtils.TimeUnit.DAY))
                company.deleteRandomCustomer();
            // Company can still acquire a new customer
            if (probabilityUtils.event(Math.abs(economy.getGrowth()), ProbabilityUtils.TimeUnit.DAY))
                company.newCustomer(dataGenerator.generateRandomCustomer());
        }
    }
}
