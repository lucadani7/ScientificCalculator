package com.lucadani;

import com.lucadani.ui.ProScientificCalculator;

import javax.swing.*;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        Locale.setDefault(Locale.US);
        SwingUtilities.invokeLater(ProScientificCalculator::new);
    }
}