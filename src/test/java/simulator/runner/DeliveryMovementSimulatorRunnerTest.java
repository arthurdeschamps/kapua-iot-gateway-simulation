package simulator.runner;

import company.address.Coordinates;
import company.delivery.Delivery;
import company.main.Company;
import company.order.Order;
import economy.Economy;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import simulator.generator.CompanyGenerator;
import simulator.generator.DataGenerator;

import java.util.Optional;
import java.util.logging.Logger;

/**
 * Test for DeliveryMovementSimulatorRunner class
 *
 * @author Arthur Deschamps
 */
public class DeliveryMovementSimulatorRunnerTest {

    private static DeliveryMovementSimulatorRunner deliveryMovementSimulatorRunner;
    private static EconomySimulatorRunner economySimulatorRunner;
    private static CompanySimulatorRunner companySimulatorRunner;
    private static Company company;
    private static Economy economy;

    @BeforeClass
    public static void setUp() {
        company = new CompanyGenerator().generateRandomCompany();
        economy = new Economy();
        economySimulatorRunner = new EconomySimulatorRunner(economy);
        companySimulatorRunner = new CompanySimulatorRunner(company,economy);
        deliveryMovementSimulatorRunner = new DeliveryMovementSimulatorRunner(company);
    }

    @Test
    public void testMoveDelivery() {
        DataGenerator dataGenerator = new DataGenerator(company);
        Optional<Order> order = dataGenerator.generateRandomOrder(company);
        Assert.assertTrue(order.isPresent());
        company.newOrder(order.get());
        Optional<Delivery> delivery = dataGenerator.generateRandomDelivery(company);
        Assert.assertTrue(delivery.isPresent());
        company.newDelivery(delivery.get());

        Assert.assertTrue(company.getDeliveries().contains(delivery.get()));
        Coordinates coordinatesBefore, coordinatesAfter;

        Logger.getGlobal().info(Double.toString(Coordinates.calculateDistance(delivery.get().getCurrentLocation(),delivery.get().getDestination().getCoordinates())));
        Logger.getGlobal().info("Addresse départ: "+delivery.get().getDeparture());
        Logger.getGlobal().info("Addresse arrivée:"+delivery.get().getDestination());
        Logger.getGlobal().info("Vitesse transport:"+delivery.get().getTransporter().getMaxSpeed());
        // Test that the delivery arrives at some point
        while (company.getDeliveries().contains(delivery.get())) {
            coordinatesBefore = delivery.get().getCurrentLocation();
            for (int i = 0; i < 10000; i++) {
                deliveryMovementSimulatorRunner.run();
            }
            coordinatesAfter = delivery.get().getCurrentLocation();

            Assert.assertNotEquals(coordinatesBefore, coordinatesAfter);

            Logger.getGlobal().info(Double.toString(Coordinates.calculateDistance(delivery.get().getCurrentLocation(),delivery.get().getDestination().getCoordinates())));

            Assert.assertTrue(Coordinates.calculateDistance(coordinatesBefore,delivery.get().getDestination().getCoordinates())
            > Coordinates.calculateDistance(coordinatesAfter,delivery.get().getDestination().getCoordinates()));
        }

    }

    @Test
    public void testDeliveryRouting() {
        int deliveryNbr;

        for (int i = 0; i < Math.pow(10,8); i++) {

            deliveryNbr = company.getDeliveries().size();
            economySimulatorRunner.run();
            companySimulatorRunner.run();
            deliveryMovementSimulatorRunner.run();

            if (deliveryNbr > company.getDeliveries().size())
                return;

        }

        Assert.fail("No delivery ever delivered.");
    }


}