package simulator;

import company.main.Company;

/**
 * Created by Arthur Deschamps on 30.05.17.
 */
class SupplyChainControlSimulator {

    private static boolean running;
    private static Company company;

    static void main(String[] args) {
        initDefault();
        runner();
    }

    private static void initDefault(){
        // Generate default company if user didn't choose any parameter
        company = new CompanyGenerator().generateDefault();
        // Generate default data
        DefaultDataGenerator.generateDefaultDatabase();
    }

    private static void runner() {
        running = true;

        while (running) {

        }
    }

}
