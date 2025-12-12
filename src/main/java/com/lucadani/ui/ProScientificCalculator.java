package com.lucadani.ui;

import com.lucadani.utils.MathEvaluator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class ProScientificCalculator extends JFrame implements ActionListener, KeyListener {
    private final JTextField display;
    private final JTextArea historyArea;
    private final List<String> history = new ArrayList<>();
    private double memory = 0;
    private boolean degrees = true; // Implicit Grade

    public ProScientificCalculator() {
        setTitle("Pro Scientific Calculator");
        setSize(600, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(30, 30, 30));

        // Display
        display = new JTextField();
        display.setFont(new Font("Consolas", Font.BOLD, 32));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBackground(Color.BLACK);
        display.setForeground(Color.WHITE);
        display.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        display.addKeyListener(this);
        add(display, BorderLayout.NORTH);

        // History panel
        historyArea = new JTextArea(6, 15);
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        historyArea.setBackground(Color.DARK_GRAY);
        historyArea.setForeground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(historyArea);
        add(scroll, BorderLayout.EAST);

        // Buttons Panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 6, 6, 6));
        panel.setBackground(new Color(30, 30, 30));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] buttons = {
                "MC", "MR", "M+", "M-", "C", "CE",
                "(", ")", "sqrt", "^", "!", "Deg",
                "sin", "cos", "tan", "asin", "acos", "atan",
                "sinh", "cosh", "tanh", "log", "ln", "Rad",
                "7", "8", "9", "/", "PI", "e",
                "4", "5", "6", "*", "%", "",
                "1", "2", "3", "-", "", "",
                "0", ".", "=", "+", "", ""
        };

        for (String label : buttons) {
            JButton btn = new JButton(label);
            btn.setFont(new Font("Consolas", Font.BOLD, 14));
            btn.addActionListener(this);
            btn.setFocusable(false); // important for keyboard
            btn.setBorderPainted(false);

            // Colors
            if ("0123456789.".contains(label)) {
                btn.setBackground(new Color(60, 60, 60));
            } else if ("+-*/=^!%".contains(label)) {
                btn.setBackground(new Color(255, 140, 0));
                btn.setForeground(Color.WHITE);
            } else {
                btn.setBackground(new Color(65, 105, 225));
            }
            btn.setForeground(Color.WHITE);
            panel.add(!label.isEmpty() ? btn : new JLabel(""));
        }

        add(panel, BorderLayout.CENTER);
        setVisible(true);
        display.requestFocus();
    }

    private void evaluateExpression() {
        String originalExpr = display.getText();
        if (originalExpr.isEmpty()) {
            return;
        }
        try {
            String expr = originalExpr.replace("PI", String.valueOf(Math.PI));
            expr = expr.replace("e", String.valueOf(Math.E));
            while (expr.contains("!")) {
                int idx = expr.indexOf("!");
                int start = idx - 1;
                while (start >= 0 && (Character.isDigit(expr.charAt(start)) || expr.charAt(start) == '.')) {
                    start--;
                }
                String numStr = expr.substring(start + 1, idx);
                double val = Double.parseDouble(numStr);
                double fact = 1;
                for (int i = 2; i <= val; fact *= i++);
                expr = expr.substring(0, start + 1) + fact + expr.substring(idx + 1);
            }

            MathEvaluator evaluator = new MathEvaluator(expr, degrees);
            double result = evaluator.solve();

            String resStr = String.valueOf(result);
            if (resStr.endsWith(".0")) {
                resStr = resStr.substring(0, resStr.length() - 2);
            }

            history.add(expr + " = " + resStr);
            StringBuilder sb = new StringBuilder();
            int start = Math.max(0, history.size() - 8);
            for (int i = start; i < history.size(); i++) {
                sb.append(history.get(i)).append("\n");
            }
            historyArea.setText(sb.toString());
            display.setText(resStr);

        } catch (Exception e) {
            display.setText("Error");
            System.err.println(e.getMessage());
        }
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        display.requestFocus();
        switch (cmd) {
            case "=" -> evaluateExpression();
            case "C" -> display.setText("");
            case "Back" -> {
                String txt = display.getText();
                if (!txt.isEmpty()) {
                    display.setText(txt.substring(0, txt.length() - 1));
                }
            }
            case "MC" -> memory = 0;
            case "MR" -> display.setText(display.getText() + (double)memory);
            case "M+" -> {
                try {
                    memory += Double.parseDouble(display.getText());
                } catch (Exception ex) {}
            }
            case "M-" -> {
                try {
                    memory -= Double.parseDouble(display.getText());
                } catch (Exception ex) {}
            }
            case "Deg" -> {
                degrees = true;
                setTitle("Scientific Calculator (Mode: DEG)");
            }
            case "Rad" -> {
                degrees = false;
                setTitle("Scientific Calculator (Mode: RAD)");
            }
            default -> display.setText(display.getText() + cmd);
        }
    }

    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {
        char key = e.getKeyChar();
        // allowing only mathematical characters
        if ("0123456789.()+-*/^%".indexOf(key) != -1) {
            display.setText(display.getText() + key);
        } else if (key == '\n') { // Enter
            evaluateExpression();
        } else if (key == '\b') { // Backspace
            String txt = display.getText();
            if (!txt.isEmpty()) {
                display.setText(txt.substring(0, txt.length() - 1));
            }
        }
    }

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {

    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }
}
