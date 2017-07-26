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
