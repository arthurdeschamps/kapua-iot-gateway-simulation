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

/**
 * Describes an address (street, city, country, zip, coordinates, etc).
 * @author Arthur Deschamps
 * @since 1.0
 */
public class Address {

    private String street;
    private String city;
    private String region;
    private String country;
    private String zip;
    private Coordinates coordinates;

    public Address(String street, String city, String region, String country, String zip, Coordinates coordinates) {
        this.street = street;
        this.city = city;
        this.region = region;
        this.country = country;
        this.zip = zip;
        this.coordinates = coordinates;
    }

    public Address(Address addressToCopy) {
        this.street  = addressToCopy.street;
        this.city = addressToCopy.city;
        this.region = addressToCopy.region;
        this.country = addressToCopy.region;
        this.zip = addressToCopy.zip;
        this.coordinates = new Coordinates(addressToCopy.coordinates.getLatitude(), addressToCopy.coordinates.getLongitude());
    }

    @Override
    public String toString() {
        return this.getZip()+" "+this.getStreet()+" "+this.getCity()+" "+this.getRegion()+" "+this.getCountry();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (!getStreet().equals(address.getStreet())) return false;
        if (!getCity().equals(address.getCity())) return false;
        if (!getRegion().equals(address.getRegion())) return false;
        if (!getCountry().equals(address.getCountry())) return false;
        return getZip().equals(address.getZip());
    }

    @Override
    public int hashCode() {
        int result = getStreet().hashCode();
        result = 31 * result + getCity().hashCode();
        result = 31 * result + getRegion().hashCode();
        result = 31 * result + getCountry().hashCode();
        result = 31 * result + getZip().hashCode();
        return result;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}
