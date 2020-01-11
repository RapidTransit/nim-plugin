package org.nim.settings;

import lombok.Getter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Getter
public class NimSettingsForm {
    private JPanel mainPanel;
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
