import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class Clock extends JLabel implements Runnable {
    private SimpleDateFormat dateFormat;

    public Clock() {
        dateFormat = new SimpleDateFormat("hh:mm:ss a, EEE, MMM d, yyyy");
        setHorizontalAlignment(SwingConstants.CENTER);
        setBounds(0, 0, 300, 50);
        updateClock();
        Thread thread = new Thread(this);
        thread.start();
    }

    private void updateClock() {
        setText(dateFormat.format(new Date()));
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SwingUtilities.invokeLater(this::updateClock);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Clock Example");
            Clock clock = new Clock();
            frame.add(clock);
            frame.setSize(400, 200);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}