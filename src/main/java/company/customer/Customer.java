package company.customer;

import com.sun.istack.internal.Nullable;
import company.address.Address;
import storage.Item;

/**
 * Created by Arthur Deschamps on 03.06.17.
 */
public class Customer extends Item {

    private String firstName;
    private String lastName;
    private Address address;
    private String emailAddress;
    @Nullable
    private String phoneNumber;

    public Customer(String firstName, String lastName, Address address, String emailAddress, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(Customer.class)) {
            Customer customer = ((Customer) obj);
            return this.firstName.equals(customer.getFirstName()) && this.lastName.equals(customer.getLastName()) && this.address.equals(customer.getAddress());
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

    @Nullable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@Nullable String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
