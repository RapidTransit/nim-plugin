package org.nim.settings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NimSettingsForm {
    private JTextField nimHome;
    private JButton lookupHome;

    private JFileChooser lookupHomeChooser;

    private void createUIComponents() {
        lookupHomeChooser = new JFileChooser();
        lookupHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
