/*
 * ******************************************************************************
 *  * Copyright (c) 2017 Arthur Deschamps
 *  *
 *  * All rights reserved. This program and the accompanying materials
 *  * are made available under the terms of the Eclipse Public License v1.0
 *  * which accompanies this distribution, and is available at
 *  * http://www.eclipse.org/legal/epl-v10.html
 *  *
 *  * Contributors:
 *  *     Arthur Deschamps
 *  ******************************************************************************
 */

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
     static TransportationHealthState degrade(TransportationHealthState healthState) {
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
