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

package company.address;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests CoordinatesTest class
 *
 * @author Arthur Deschamps
 */
public class CoordinatesTest {

    @Test
    public void testConverter() {
        String latitude = "-1.33984";
        String longitude = "49.33389";
        Coordinates coordinates = new Coordinates(latitude,longitude);
        Assert.assertTrue(-1.33984f == coordinates.getLatitude());
        Assert.assertTrue(49.33389f == coordinates.getLongitude());
    }

    @Test
    public void testDistanceCalculator() {
        Assert.assertTrue(Coordinates.calculateDistance(new Coordinates(0f,0f),
                new Coordinates(0f,0f)) == 0);

        Coordinates p1 = new Coordinates(42.990967f, -71.463767f);
        Coordinates p2 = new Coordinates(50.000000f, 34.9588f);
        Assert.assertEquals(Coordinates.calculateDistance(p1,p2), Coordinates.calculateDistance(p2,p1),1e-15);
        Assert.assertEquals(Coordinates.calculateDistance(p1,p2),7480,50);
    }
}
