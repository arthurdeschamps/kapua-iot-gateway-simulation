package company.customer;

import com.sun.istack.internal.Nullable;
import company.transportation.PostalAddress;
import jedis.JedisObject;

/**
 * Created by Arthur Deschamps on 03.06.17.
 */
public class Customer extends JedisObject {

    private String firstName;
    private String lastName;
    private PostalAddress postalAddress;
    private String emailAddress;
    @Nullable
    private String phoneNumber;

    public Customer(String firstName, String lastName, PostalAddress postalAddress, String emailAddress, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.postalAddress = postalAddress;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Nullable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@Nullable String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
