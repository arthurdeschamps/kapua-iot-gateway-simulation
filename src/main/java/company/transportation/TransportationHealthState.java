package company.transportation;

/**
 * Possible health states of a transportation
 * @since 1.0
 * @author Arthur Deschamps
 */
public enum TransportationHealthState {
    PERFECT, GOOD, ACCEPTABLE, BAD, CRITICAL;

    /**
     * Computes the next state of degradation for a given health state.
     * @param healthState
     * Health state to degrade.
     * @return
     * Next state of degradation from @healthState. If this last parameter is already the worse health state, returns
     * the same state.
     */
    public TransportationHealthState degrade(TransportationHealthState healthState) {
        switch (healthState) {
            case PERFECT:
                return GOOD;
            case GOOD:
                return ACCEPTABLE;
            case ACCEPTABLE:
                return BAD;
            case BAD:
                return CRITICAL;
            case CRITICAL:
                return CRITICAL;
            default:
                return healthState;
        }
    }
}
