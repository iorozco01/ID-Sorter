package ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

import ui.*;

//creates a keyPad with letters from A to Z and clear
public class KeyPad extends JPanel implements KeyListener {
    private static final String CLR_STR = "CLR";
    private static final String LOAD_STR = "LOAD";
    private JButton[] keys;
    private JLabel label;
    private String code;
    private ClickHandler keyHandler;

    /**
     * Constructor creates keypad and code display area.
     */
    /*
    EFFECTS: creates a keypad with letters and keys detailed
     */
    public KeyPad() {
        code = "";
        keyHandler = new ClickHandler();
        setLayout(new BorderLayout());
        JPanel keyPanel = new JPanel();
        keyPanel.setLayout(new GridLayout(3,9));
        addButtons(keyPanel);
        add(keyPanel, BorderLayout.CENTER);
        label = new JLabel(getLabel());
        Box hbox = Box.createHorizontalBox();
        hbox.add(Box.createHorizontalGlue());
        hbox.add(label);
        hbox.add(Box.createHorizontalGlue());
        add(hbox, BorderLayout.NORTH);
    }

    /**
     * Gets code entered.
     * @return  code entered
     */
    public String getCode() {
        return code;
    }

    /**
     * Clears the code entered on the keypad
     */
    /*
    MODIFIES: code
    EFFECTS: erases current inputted code
     */
    public void clearCode() {
        code = "";
        label.setText(getLabel());
        label.repaint();
    }

    /**
     * Adds buttons to button panel
     * @param p  the button panel
     */
    /*
    EFFECTS: creates pseudo keyboard with letters from A to Z and clear
     */
    private void addButtons(JPanel p) {
        keys = new JButton[26];
        int n = 65;

        for (int i = 0; i < 26; i++) {
            keys[i] = new JButton(String.valueOf((char)n));
            keys[i].addActionListener(keyHandler);
            p.add(keys[i]);
            n++;
        }

        keys[25] = new JButton(CLR_STR);
        keys[25].addActionListener(keyHandler);
        p.add(keys[25]);
    }

    /**
     * Gets label for code display area
     * @return  label for code display area
     */
    private String getLabel() {
        String label = "";
        label = code;

        return label;
    }

    /**
     * A listener for key events.
     */
    private class ClickHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton src = (JButton) e.getSource();

            if (src.getText().equals(CLR_STR)) {
                code = "";
            } else if (code.length() < CountryCode.NUM_DIGITS) {
                code = code + src.getText();
            }

            label.setText(getLabel());
            label.repaint();
        }
    }



    @Override
    public void keyPressed(KeyEvent ke) {

    }

    @Override
    public void keyReleased(KeyEvent ke) {

    }

    @Override
    public void keyTyped(KeyEvent ke) {

    }

}
