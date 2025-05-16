package display;

import javax.swing.JOptionPane;

public class JOptionPaneMessageDisplay implements MessageDisplay {
    @Override
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}
