import java.io.Serializable;

class Rental implements Serializable {
    private Vehicle vehicle;
    private Customer customer;
    private String rentalDate;
    private String returnDate;

    public Rental(Vehicle vehicle, Customer customer, String rentalDate, String returnDate) {
        this.vehicle = vehicle;
        this.customer = customer;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setRentalDate(String rentalDate) {
        this.rentalDate = rentalDate;
    }

    public String getRentalDate() {
        return rentalDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getReturnDate() {
        return returnDate;
    }
}
