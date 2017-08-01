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

import com.github.javafaker.Faker;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Arthur Deschamps on 09.06.17.
 */
public class GeneratorsTest {

    @Test
    public void testRandomGenerations() {
        Assert.assertNotNull(AddressGenerator.generateInternationalAddress());
        Assert.assertNotNull(DataGenerator.generateRandomProductType());
        Assert.assertNotNull(AddressGenerator.generateLocalAddress(new Faker().address().cityName()));
    }

//    @Test
//    public void testLocaleAddressGeneration() {
//        Address address = AddressGenerator.generateNationalAddress(new Locale("fr","FR"));
//        Assert.assertEquals(address.getCountry(),"France");
//    }
}
