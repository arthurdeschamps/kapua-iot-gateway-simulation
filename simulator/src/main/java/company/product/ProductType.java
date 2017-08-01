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

package company.product;

import storage.Item;


/**
 * Created by Arthur Deschamps on 30.05.17.
 * This class represents a type of product sold by the company.
 * It is the meta-class of the class Product
 */
public class ProductType extends Item {

    private String name;
    private String productionCountry;
    private float basePrice; // in USD
    private float weight; // in grams
    private boolean fragile;


    public ProductType(String name, String productionCountry, float price, float weight, boolean fragile) {
        this.name = name;
        this.productionCountry = productionCountry;
        this.basePrice = price;
        this.weight = weight;
        this.fragile = fragile;
    }


    // Validate object before saving
    public boolean validate() {
        return ((basePrice > 0) && (weight > 0));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductType that = (ProductType) o;

        if (Float.compare(that.getBasePrice(), getBasePrice()) != 0) return false;
        if (Float.compare(that.getWeight(), getWeight()) != 0) return false;
        if (isFragile() != that.isFragile()) return false;
        if (!getName().equals(that.getName())) return false;
        return getProductionCountry().equals(that.getProductionCountry());
    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getProductionCountry().hashCode();
        result = 31 * result + (getBasePrice() != +0.0f ? Float.floatToIntBits(getBasePrice()) : 0);
        result = 31 * result + (getWeight() != +0.0f ? Float.floatToIntBits(getWeight()) : 0);
        result = 31 * result + (isFragile() ? 1 : 0);
        return result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(float basePrice) {
        this.basePrice = basePrice;
    }

    public String getProductionCountry() {
        return productionCountry;
    }

    public float getWeight() {
        return weight;
    }

    public boolean isFragile() {
        return fragile;
    }

    public void setProductionCountry(String productionCountry) {
        this.productionCountry = productionCountry;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setFragile(boolean fragile) {
        this.fragile = fragile;
    }

}
