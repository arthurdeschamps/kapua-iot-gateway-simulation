package simulation.generators;

/**
 * Wrapper for com.github.javafaker.Faker
 * @since 1.0
 * @see com.github.javafaker.Faker
 * @author Arthur Deschamps
 */
public class Faker {
    private static final com.github.javafaker.Faker instance = new com.github.javafaker.Faker();

    public static com.github.javafaker.Faker getInstance() {
        return instance;
    }
}
