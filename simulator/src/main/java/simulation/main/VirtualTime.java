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

package simulation.main;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Handles virtual time.
 *
 * @author Arthur Deschamps
 */
public class VirtualTime {

    private Date initDate;
    private SimpleDateFormat formatter;

    private Parametrizer parametrizer;

    public VirtualTime(Parametrizer parametrizer) {
        this.parametrizer = parametrizer;
        this.initDate = new Date();
        this.formatter = new SimpleDateFormat("dd/MM/yy '-' hh'h'");
    }

    /**
     * @return
     * The current virtual time in a displayable format.
     */
    public String getCurrentDateTime() {
        return format(computeCurrentDate());
    }

    /**
     * Computes the current date and time in the virtual environment.
     * @return
     * The virtual current date time.
     */
    private Date computeCurrentDate() {
        return new Date(initDate.getTime() + (new Date().getTime() - initDate.getTime()) * parametrizer.getTimeFlow());
    }

    /**
     * Format a date.
     * @param date
     * A Date object.
     * @return
     * A user/UI-friendly formatted date.
     */
    private String format(Date date) {
        return formatter.format(date);
    }
}
