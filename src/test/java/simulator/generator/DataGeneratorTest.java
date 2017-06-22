package simulator.generator;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Arthur Deschamps on 09.06.17.
 */
public class DataGeneratorTest {

    @Test
    public void testRandomGenerations() {
        Assert.assertNotNull(DataGenerator.generateRandomAddress());
        Assert.assertNotNull(DataGenerator.generateRandomProductType());
    }
}
