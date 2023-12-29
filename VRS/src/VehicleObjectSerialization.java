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

        List<Vehicle> vehicles = (List<Vehicle>) data.get(0);
        List<Rental> rentals = (List<Rental>) data.get(1);

        JFrame frame = new JFrame("Vehicle Rental System : TriadAuto Rentals");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1400, 800);

        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 6) {
                    return ImageIcon.class;
                }
                return super.getColumnClass(columnIndex);
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

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAME))) {

                tableModel.setRowCount(0);

                for (Vehicle vehicle : vehicles) {
                    ImageIcon imageIcon = new ImageIcon(vehicle.getImagePath());
                    Image image = imageIcon.getImage().getScaledInstance(100, -1, Image.SCALE_SMOOTH);
                    ImageIcon scaledImageIcon = new ImageIcon(image);
                    String customerName = "";

                    for (Rental rental : rentals) {
                        if (rental.getVehicle().getVehicleID().equals(vehicle.getVehicleID())) {
                            if (rental.getCustomer() != null) {
                                customerName = rental.getCustomer().getName();
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
                            customerName
                    };
                    tableModel.addRow(rowData);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            vehicleTable.getColumnModel().getColumn(6).setCellRenderer(new ImageRenderer());
            vehicleTable.setRowHeight(100);

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