import java.io.Serializable;

enum BloodGroup {
    A_POSITIVE, A_NEGATIVE, B_POSITIVE, B_NEGATIVE, AB_POSITIVE, AB_NEGATIVE, O_POSITIVE, O_NEGATIVE
}

public class Customer implements Serializable {
    private String customerLicenseID;
    private String name;
    private String phoneNumber;
    private BloodGroup bloodGroup;
    private Date dateOfBirth;

    public Customer(String customerLicenseID, String name, String phoneNumber, BloodGroup bloodGroup, Date dateOfBirth) {
        this.customerLicenseID = customerLicenseID;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.bloodGroup = bloodGroup;
        this.dateOfBirth = dateOfBirth;
    }

    public void setCustomerLicenseID(String customerLicenseID) {
        this.customerLicenseID = customerLicenseID;
    }

    public String getCustomerLicenseID() {
        return customerLicenseID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setRentedCustomerName(String name) {
        this.name = name;
    }

    public String getRentedCustomerName() {
        return name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }
}