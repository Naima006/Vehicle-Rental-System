import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class VehicleObjectSerialization {
    private static final String FILENAME = "vehicle_data.ser";
    public static void serialize(List<Vehicle> vehicles) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            oos.writeObject(vehicles);
            System.out.println("Serialization successful. Data saved to " + FILENAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void displayDataGUI() {
        List<Object> data = VehicleObjectSerialization.deserializeData();
    
        @SuppressWarnings("unchecked")
        List<Vehicle> vehicles = (List<Vehicle>) data.get(0);
        @SuppressWarnings("unchecked")
        List<Rental> rentals = (List<Rental>) data.get(1);
    
        JFrame frame = new JFrame("Vehicle Rental System : TriadAuto Rentals");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 800);

        Clock clock = new Clock();
        frame.add(clock, BorderLayout.SOUTH);

        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 6) {
                    return ImageIcon.class;
                }
                return super.getColumnClass(columnIndex);
            }
    
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable vehicleTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(vehicleTable);
    
        tableModel.addColumn("Vehicle ID");
        tableModel.addColumn("Type");
        tableModel.addColumn("Make");
        tableModel.addColumn("Model");
        tableModel.addColumn("Status");
        tableModel.addColumn("Price(Per Day)");
        tableModel.addColumn("Image");
        tableModel.addColumn("Rented By");
        tableModel.addColumn("Rental Date");
        tableModel.addColumn("Return Date");
    
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAME))) {
    
            tableModel.setRowCount(0);
    
            for (Vehicle vehicle : vehicles) {
                ImageIcon imageIcon = new ImageIcon(vehicle.getImagePath());
                Image image = imageIcon.getImage().getScaledInstance(100, -1, Image.SCALE_SMOOTH);
                ImageIcon scaledImageIcon = new ImageIcon(image);
                String customerInfo = "";
                String rentalDateS = "";
                String returnDateS = "";
    
                for (Rental rental : rentals) {
                    if (rental.getVehicle().getVehicleID().equals(vehicle.getVehicleID())) {
                        if (rental.getCustomer() != null) {
                            Customer customer = rental.getCustomer();
                            customerInfo = "NAME: " + customer.getName() + "  |  LID: " + customer.getCustomerLicenseID() + "  |  PHONE: " + customer.getPhoneNumber() + "  |  BG: " + customer.getBloodGroup();
                            rentalDateS = rental.getRentalDate() != null ? rental.getRentalDate().toString() : "";
                            returnDateS = rental.getReturnDate() != null ? rental.getReturnDate().toString() : "";
                            break;
                        }
                    }
                }
                Object[] rowData = {
                        vehicle.getVehicleID(),
                        vehicle.getType(),
                        vehicle.getMake(),
                        vehicle.getModel(),
                        vehicle.getStatus(),
                        vehicle.getCost(),
                        scaledImageIcon,
                        customerInfo,
                        rentalDateS,
                        returnDateS
                };
                tableModel.addRow(rowData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        vehicleTable.getColumnModel().getColumn(6).setCellRenderer(new ImageRenderer());
        vehicleTable.setRowHeight(100);

        for (int column = 0; column < vehicleTable.getColumnCount(); column++) {
            int maxWidth = 0;
            for (int row = 0; row < vehicleTable.getRowCount(); row++) {
                TableCellRenderer cellRenderer = vehicleTable.getCellRenderer(row, column);
                Object value = vehicleTable.getValueAt(row, column);
                Component cellComponent = cellRenderer.getTableCellRendererComponent(vehicleTable, value, false, false, row, column);
                maxWidth = Math.max(maxWidth, cellComponent.getPreferredSize().width);
            }
            TableColumn tableColumn = vehicleTable.getColumnModel().getColumn(column);
            tableColumn.setPreferredWidth(maxWidth);
        }
    
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }
    
    public static void serializeData(List<Vehicle> vehicles, List<Rental> rentals) {
        List<Object> dataToSerialize = new ArrayList<>();
        dataToSerialize.add(vehicles);
        dataToSerialize.add(rentals);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            oos.writeObject(dataToSerialize);
            System.out.println("Serialization successful. Data saved to " + FILENAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("unchecked")
    public static List<Object> deserializeData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAME))) {
            return (List<Object>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class ImageRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = new JLabel();
            if (value != null) {
                label.setIcon((ImageIcon) value);
                label.setHorizontalAlignment(JLabel.CENTER);
            }
            return label;
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(VehicleObjectSerialization::displayDataGUI);
        }

}