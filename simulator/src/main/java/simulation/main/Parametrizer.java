package simulation.main;

import company.company.Company;
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

    private int timeFlow;
    private long dataSendingDelay;
    private boolean displayMetrics;
    private long displayMetricsDelay;
    @Nullable
    private Economy economy;
    @Nullable
    private Company company;

    /**
     * Call this constructor to parametrize the whole simulation.
     * @param timeFlow
     * Speed of virtual time compare to real time. If timeFlow = 1, then 1 virtual second = 1 real second.
     * @param dataSendingDelay
     * Delay for periodic data sending to kapua in seconds.
     * @param displayMetrics
     * Decides if data from the company and the economy will be displayed.
     * @param displayMetricsDelay
     * If displayMetrics is true, delay at which metrics are displayed in seconds. Ignored if displayMetrics is false.
     * @param company
     * Company to use in the simulator. If null, a company is auto-generated and respect the criteria @withInitialData.
     * @param withInitialData
     * If company is null, then this parameter is looked up when generating the company.
     * @param economy
     * Economy to use in the simulation. This parameter will only affect the initial values of the Economy,
     * but not the behaviors of the Economy simulator. If the parameter is null, then the default Economy is
     * generated.
     */
    public Parametrizer(int timeFlow, long dataSendingDelay, boolean displayMetrics, long displayMetricsDelay,
                        @Nullable  Company company, boolean withInitialData, @Nullable Economy economy) {
        this.timeFlow = timeFlow;
        this.dataSendingDelay = dataSendingDelay;
        this.displayMetrics = displayMetrics;
        this.displayMetricsDelay = displayMetricsDelay;
        this.economy = economy == null ? new Economy() : economy;

        if (company == null)
            this.company = withInitialData ? CompanyGenerator.generateRandomCompany() : CompanyGenerator.generateEmptyRandomCompany();
        else
            this.company = company;
    }

    /**
     * Default parametrizer.
     */
    public Parametrizer() {
        this.timeFlow = 3600;
        this.dataSendingDelay = 3;
        this.displayMetrics = true;
        this.displayMetricsDelay = 5;
        this.company = CompanyGenerator.generateRandomCompany();
        this.economy = new Economy();
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

    public long getDataSendingDelay() {
        return dataSendingDelay;
    }

    public void setDataSendingDelay(long dataSendingDelay) {
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
