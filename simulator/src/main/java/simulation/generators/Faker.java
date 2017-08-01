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
