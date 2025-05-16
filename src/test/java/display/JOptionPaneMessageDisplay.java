package display;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class JOptionPaneMessageDisplayTest {

    @Test
    void testShowMessageDoesNotThrow() {
        MessageDisplay display = new JOptionPaneMessageDisplay();
        assertDoesNotThrow(() -> display.showMessage("Тестове повідомлення"));
    }
}
