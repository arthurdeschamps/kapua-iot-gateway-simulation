package simulation.simulators.runners;

import company.address.Coordinates;
import company.company.Company;
import company.delivery.Delivery;
import company.order.Order;
import company.transportation.Transportation;
import company.transportation.TransportationHealthState;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import simulation.generators.CompanyGenerator;
import simulation.generators.DataGenerator;

import java.util.Optional;
import java.util.logging.Logger;

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
        company = new CompanyGenerator().generateRandomCompany();
        telemetryDataSimulatorRunner = new TelemetryDataSimulatorRunner(company);
    }

    @Test
    public void testMoveDelivery() {

        DataGenerator dataGenerator = new DataGenerator(company);
        Optional<Order> order = dataGenerator.generateRandomOrder();
        Assert.assertTrue(order.isPresent());
        company.newOrder(order.get());
        Optional<Delivery> delivery = dataGenerator.generateRandomDelivery();
        Assert.assertTrue(delivery.isPresent());
        company.newDelivery(delivery.get());

        Assert.assertTrue(company.getDeliveries().contains(delivery.get()));
        Coordinates coordinatesBefore, coordinatesAfter, destination;

        destination = delivery.get().getDestination().getCoordinates();

        coordinatesBefore = delivery.get().getCurrentLocation();
        Logger.getGlobal().info("Distance to achieve: "+Double.toString(Coordinates.calculateDistance(coordinatesBefore, destination)));
        Logger.getGlobal().info("Transportation speed:"+delivery.get().getTransporter().getActualSpeed());
        // Test that the delivery arrives at some point
        for (int i = 0; i < 500; i++)
            telemetryDataSimulatorRunner.run();

        coordinatesAfter = delivery.get().getCurrentLocation();

        Assert.assertNotEquals(coordinatesBefore, coordinatesAfter);

        Logger.getGlobal().info(Double.toString(Coordinates.calculateDistance(coordinatesAfter,delivery.get().getDestination().getCoordinates())));

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


}