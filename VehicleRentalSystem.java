import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;

public class VehicleRentalSystem extends JFrame {
    private JTable vehicleTable;
    private DefaultTableModel tableModel;
    private JTextField customerLicenseIDField;
    private JTextField customerNameField;
    private JTextField phoneNumberField;
    private JComboBox<BloodGroup> bloodGroupComboBox;
    private JFormattedTextField dobField;
    private JFormattedTextField rentalDateField;
    private JFormattedTextField returnDateField;
    private JButton rentButton;
    private Clock clock;

    private List<Vehicle> vehicles;
    private List<Rental> rentals;

    public VehicleRentalSystem(List<Vehicle> vehicles, List<Rental> rentals) {
        this.vehicles = vehicles;
        this.rentals = rentals;

        setTitle("Vehicle Customer Service : TriadAuto Rentals");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 800);

        initializeTable();

        JScrollPane scrollPane = new JScrollPane(vehicleTable);
        add(scrollPane, BorderLayout.CENTER);

        clock = new Clock();
        add(clock, BorderLayout.SOUTH);

        JPanel customerPanel = new JPanel();
        customerLicenseIDField = new JTextField(5);
        customerNameField = new JTextField(5);
        phoneNumberField = new JTextField(5);
        bloodGroupComboBox = new JComboBox<>(BloodGroup.values());
        dobField = new JFormattedTextField();
        dobField.setColumns(5);
        rentalDateField = new JFormattedTextField();
        rentalDateField.setColumns(5);
        returnDateField = new JFormattedTextField();
        returnDateField.setColumns(5);

        rentButton = new JButton("Rent");
        rentButton.addActionListener(e -> rentSelectedVehicle());

        customerPanel.add(new JLabel("Customer License ID: "));
        customerPanel.add(customerLicenseIDField);
        customerPanel.add(new JLabel("Customer Name: "));
        customerPanel.add(customerNameField);
        customerPanel.add(new JLabel("Phone Number: "));
        customerPanel.add(phoneNumberField);
        customerPanel.add(new JLabel("Blood Group: "));
        customerPanel.add(bloodGroupComboBox);
        customerPanel.add(new JLabel("Date of Birth (MM/DD/YYYY): "));
        customerPanel.add(dobField);
        customerPanel.add(new JLabel("Rental Date (MM/DD/YYYY): "));
        customerPanel.add(rentalDateField);
        customerPanel.add(new JLabel("Return Date (MM/DD/YYYY): "));
        customerPanel.add(returnDateField);

        customerPanel.add(rentButton);
        add(customerPanel, BorderLayout.NORTH);

        populateTable();
    }

    public List<Rental> getRentals() {
        return rentals;
    }

    public void updateSerializedData() {
        VehicleObjectSerialization.serialize(vehicles);
    }

    private void initializeTable() {
        tableModel = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 6) {
                    return ImageIcon.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };
        vehicleTable = new JTable(tableModel);

        tableModel.addColumn("Vehicle ID");
        tableModel.addColumn("Type");
        tableModel.addColumn("Make");
        tableModel.addColumn("Model");
        tableModel.addColumn("Status");
        tableModel.addColumn("Price(Per Day)");
        tableModel.addColumn("Image");

        vehicleTable.getColumnModel().getColumn(6).setCellRenderer(new ImageRenderer());

        vehicleTable.setRowHeight(100);
    }

    private void populateTable() {
        for (Vehicle vehicle : vehicles) {
            ImageIcon imageIcon = new ImageIcon(vehicle.getImagePath());
            Image image = imageIcon.getImage().getScaledInstance(150, 90, Image.SCALE_SMOOTH);
            ImageIcon scaledImageIcon = new ImageIcon(image);

            Object[] rowData = {
                    vehicle.getVehicleID(),
                    vehicle.getType(),
                    vehicle.getMake(),
                    vehicle.getModel(),
                    vehicle.getStatus(),
                    vehicle.getCost(),
                    scaledImageIcon,
            };

            tableModel.addRow(rowData);
        }
    }

    private class ImageRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = new JLabel();
            if (value != null) {
                label.setIcon((ImageIcon) value);
                label.setHorizontalAlignment(JLabel.CENTER);

                RentalStatus status = (RentalStatus) table.getModel().getValueAt(row, 4);

                if (status == RentalStatus.RENTED) {
                    label.setBackground(Color.RED);
                } else {
                    label.setBackground(Color.GREEN);
                }
            }
            return label;
        }
    }

    private Vehicle getVehicleByID(String vehicleID) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getVehicleID().equals(vehicleID)) {
                return vehicle;
            }
        }
        return null;
    }

    private void rentSelectedVehicle() {
        int selectedRow = vehicleTable.getSelectedRow();
        if (selectedRow >= 0) {
            String vehicleID = (String) tableModel.getValueAt(selectedRow, 0);
            Vehicle selectedVehicle = getVehicleByID(vehicleID);

            String customerLicenseID = customerLicenseIDField.getText();
            String customerName = customerNameField.getText();
            String phoneNumber = phoneNumberField.getText();
            BloodGroup bloodGroup = (BloodGroup) bloodGroupComboBox.getSelectedItem();
            Date dob = (Date) dobField.getValue();
            Date rentalDate = (Date) rentalDateField.getValue();
            Date returnDate = (Date) returnDateField.getValue();

            if (selectedVehicle != null) {
                if (selectedVehicle.getStatus() == RentalStatus.AVAILABLE) {
                    if (customerLicenseID.isEmpty() && customerName.isEmpty() && phoneNumber.isEmpty() && dob == null && rentalDate == null && returnDate == null) {
                        JOptionPane.showMessageDialog(this, "Please fill all customer information and dates.");
                    } else {
                        Customer customer = new Customer(customerLicenseID, customerName, phoneNumber, bloodGroup, dob);
                        Rental rental = new Rental(selectedVehicle, customer, rentalDate, returnDate);
                        rentals.add(rental);
                        processRental(selectedVehicle, customer, selectedRow);
                        JOptionPane.showMessageDialog(this, "The selected vehicle is rented by " + customer.getName());
                        VehicleObjectSerialization.serializeData(vehicles, rentals);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "The selected vehicle is not available for rent.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a vehicle to rent.");
        }
    }

    private void processRental(Vehicle vehicle, Customer customer, int selectedRow) {
        vehicle.setStatus(RentalStatus.RENTED);
        updateSerializedData();
        System.out.println("Vehicle " + vehicle.getVehicleID() + " rented by " + customer.getName());
        customerLicenseIDField.setText("");
        customerNameField.setText("");
        phoneNumberField.setText("");
        dobField.setText("");
        rentalDateField.setText("");
        returnDateField.setText("");
        tableModel.setValueAt(RentalStatus.RENTED, selectedRow, 4);
        updateSerializedData();
    }

    public static void main(String[] args) {
        List<Vehicle> vehicles = new ArrayList<>();
        List<Rental> rentals = new ArrayList<>();

        vehicles.add(new Vehicle("V001", VehicleType.SEDAN, "Toyota", "Corolla", RentalStatus.AVAILABLE, "BDT 5,500", "sedan.png"));
        vehicles.add(new Vehicle("V002", VehicleType.SUV, "Honda", "CR-V", RentalStatus.RENTED, "BDT 7,500", "suv.png"));
        vehicles.add(new Vehicle("V003", VehicleType.MINIBUS, "Toyota", "Coaster", RentalStatus.AVAILABLE, "BDT 15,000", "minibus.png"));
        vehicles.add(new Vehicle("V004", VehicleType.AMBULANCE, "Toyota", "HiMedic", RentalStatus.AVAILABLE, "BDT 10,000", "ambulance.png"));
        vehicles.add(new Vehicle("V005", VehicleType.MINIVAN, "Toyota", "Noah", RentalStatus.AVAILABLE, "BDT 9,000", "minivan.png"));
        vehicles.add(new Vehicle("V006", VehicleType.TRUCK, "Ford", "F-150", RentalStatus.UNDER_MAINTENANCE, "BDT 4,500", "truck.png"));

        VehicleObjectSerialization.serialize(vehicles);

        SwingUtilities.invokeLater(() -> {
            VehicleRentalSystem gui = new VehicleRentalSystem(vehicles, rentals);
            gui.setVisible(true);
        });
    }
}
