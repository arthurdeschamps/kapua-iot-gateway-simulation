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

package simulation.main;

import company.company.Company;
import company.company.CompanyType;
import economy.Economy;
import org.jetbrains.annotations.Nullable;
import simulation.generators.CompanyGenerator;
import simulation.simulators.SupplyChainControlSimulator;
import simulation.simulators.runners.AbstractRunner;

/**
 * The class allows to totally parametrize the whole simulation at will.
 * @author Arthur Deschamps
 * @since 1.0
 * @see SupplyChainControlSimulator
 */
public class Parametrizer {

    /** Speed of virtual time compare to real time. If timeFlow = 1, then 1 virtual second = 1 real second. **/
    private int timeFlow;
    /** Delay for periodic data sending to kapua in seconds. **/
    private int dataSendingDelay;
    /** Decides if data from the company and the economy will be displayed. **/
    private boolean displayMetrics;
    /** If displayMetrics is true, delay at which metrics are displayed in seconds. Ignored if displayMetrics is false.**/
    private long displayMetricsDelay;
    /** Economy to use in the simulation. This parameter will only affect the initial values of the Economy,
     * but not the behaviors of the Economy simulator. If the parameter is null, then the default Economy is
     * generated.
     **/
    private Economy economy;
    /** Company to use in the simulator. If null, a company is auto-generated and respect the criteria @withInitialData.**/
    private Company company;
    /** Simulation has to be accesible if parametrizer needs to restart it. **/
    private SupplyChainControlSimulator simulation;

    /**
     * Default parametrizer.
     */
    public Parametrizer(SupplyChainControlSimulator simulation) {
        this.timeFlow = 3600;
        this.dataSendingDelay = 3;
        this.displayMetrics = true;
        this.displayMetricsDelay = 5;
        this.company = CompanyGenerator.generateRandomCompany();
        this.economy = new Economy();
        this.simulation = simulation;
    }

    /**
     * Converts the time flow to a delay in milliseconds to execute the runners
     * @return
     * A delay in microseconds.
     */
    public long getDelayInMilliSeconds() {
        switch (AbstractRunner.getTimeUnit()) {
            case HOUR:
                // One execution of the simulation = 1 real hour = 3.6e6 ms
                return (long)(3.6*Math.pow(10,6)/this.getTimeFlow());
            default:
                throw new UnsupportedOperationException("The only supported time unit for runners is Hour.");
        }
    }

    public int getTimeFlow() {
        return timeFlow;
    }

    public void setTimeFlow(int timeFlow) {
        this.timeFlow = timeFlow;
    }

    public int getDataSendingDelay() {
        return dataSendingDelay;
    }

    public void setDataSendingDelay(int dataSendingDelay) {
        this.dataSendingDelay = dataSendingDelay;
    }

    public boolean isDisplayMetrics() {
        return displayMetrics;
    }

    public void setDisplayMetrics(boolean displayMetrics) {
        this.displayMetrics = displayMetrics;
    }

    public long getDisplayMetricsDelay() {
        return displayMetricsDelay;
    }

    public void setDisplayMetricsDelay(long displayMetricsDelay) {
        this.displayMetricsDelay = displayMetricsDelay;
    }

    public Economy getEconomy() {
        return economy;
    }

    public void setEconomy(Economy economy) {
        this.economy = economy;
    }

    public void setCompanyType(CompanyType companyType) {
        company.setType(companyType);
        simulation.restart();
    }

    public Company getCompany() {
        return company;
    }
}
