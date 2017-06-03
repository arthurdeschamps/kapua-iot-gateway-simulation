package company.customer;

import jedis.JedisObject;

/**
 * Created by Arthur Deschamps on 03.06.17.
 */
public class Customer extends JedisObject {

    private String firstName;
    private String lastName;
    private PostalAddress postalAddress;

    public Customer(String firstName, String lastName, PostalAddress postalAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.postalAddress = postalAddress;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(Customer.class)) {
            Customer customer = ((Customer) obj);
            return this.firstName.equals(customer.getFirstName()) && this.lastName.equals(customer.getLastName()) && this.postalAddress.equals(customer.getPostalAddress());
        }
        return false;
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

    public PostalAddress getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(PostalAddress postalAddress) {
        this.postalAddress = postalAddress;
    }
}
