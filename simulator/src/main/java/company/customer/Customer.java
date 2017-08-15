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

package company.customer;

import company.address.Address;
import storage.Item;

/**
 * Describes a customer.
 * @author Arthur Deschamps
 * @since 1.0
 */
public class Customer extends Item {

    private String firstName;
    private String lastName;
    private Address address;
    private String emailAddress;
    private String phoneNumber;

    public Customer(String firstName, String lastName, Address address, String emailAddress, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }

    public Customer(Customer customerToCopy) {
        this.firstName = customerToCopy.firstName;
        this.lastName = customerToCopy.lastName;
        this.emailAddress = customerToCopy.emailAddress;
        this.phoneNumber = customerToCopy.phoneNumber;
        this.address = new Address(customerToCopy.address);
    }

    /**
     * A customer is uniquely identified by the triplet (first name, last name, address).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (!getFirstName().equals(customer.getFirstName())) return false;
        if (!getLastName().equals(customer.getLastName())) return false;
        return getAddress().equals(customer.getAddress());
    }

    @Override
    public int hashCode() {
        int result = getFirstName().hashCode();
        result = 31 * result + getLastName().hashCode();
        result = 31 * result + getAddress().hashCode();
        return result;
    }

    @Override
    public boolean validate() {
        return ((firstName != null) && (lastName != null));
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
