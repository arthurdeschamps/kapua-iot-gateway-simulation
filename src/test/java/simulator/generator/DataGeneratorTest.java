package simulator.generator;

import org.junit.Assert;
import org.junit.Test;
import simulator.generator.DataGenerator;

/**
 * Created by Arthur Deschamps on 09.06.17.
 */
public class DataGeneratorTest {

    @Test
    public void testRandomGenerations() {
        Assert.assertNotNull(DataGenerator.getInstance().generateRandomAddress());
        Assert.assertNotNull(DataGenerator.getInstance().generateRandomProductType());
    }
}
