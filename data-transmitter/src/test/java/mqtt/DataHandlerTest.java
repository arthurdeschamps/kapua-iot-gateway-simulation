package mqtt;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Description..
 *
 * @author Arthur Deschamps
 */
public class DataHandlerTest {

    private static final Logger logger = LoggerFactory.getLogger(DataHandlerTest.class);

    private final String appId = "appId";
    private final String pubId = "pubId";
    private final String pubAccName = "pubAccName";

    @Test
    public void testHandle() {
    }

    private String topic(final String... topic) {
        return Stream.concat(
                Stream.of(pubAccName,pubId, appId),
                Arrays.stream(topic))
                .collect(Collectors.joining("/"));
    }
}
