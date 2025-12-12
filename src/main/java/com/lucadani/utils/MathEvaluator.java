package com.lucadani.utils;

import java.util.function.BiFunction;

public class MathEvaluator {
    private final String expression;
    private final boolean degrees;
    private int pos = -1;
    private int ch;

    public MathEvaluator(String expression, boolean degrees) {
        this.expression = expression;
        this.degrees = degrees;
    }

    public double solve() {
        nextChar();
        double x = parseExpression();
        if (pos < expression.length()) {
            throw new RuntimeException("Unexpected: " + (char)ch);
        }
        return x;
    }

    private void nextChar() {
        ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
    }

    private boolean eat(int charToEat) {
        while (ch == ' ') {
            nextChar();
        }
        if (ch == charToEat) {
            nextChar();
            return true;
        }
        return false;
    }

    private double parseExpression() {
        double x = parseTerm();
        while (true) {
            if (eat('+')) {
                x += parseTerm(); // adunare
            } else if (eat('-')) {
                x -= parseTerm(); // scadere
            } else {
                return x;
            }
        }
    }

    private double parseTerm() {
        double x = parseFactor();
        while (true) {
            if (eat('*')) {
                x *= parseFactor();
            } else if (eat('/')) {
                x /= parseFactor();
            } else if (eat('%')) {
                x %= parseFactor();
            } else {
                return x;
            }
        }
    }

    private double parseFactor() {
        BiFunction<String, Double, Double> applyFunction = (expr, value) -> switch (expr) {
            case "sqrt" -> Math.sqrt(value);
            case "sin" -> Math.sin(degrees ? Math.toRadians(value) : value);
            case "cos" -> Math.cos(degrees ? Math.toRadians(value) : value);
            case "tan"  -> Math.tan(degrees ? Math.toRadians(value) : value);
            case "asin" -> degrees ? Math.toDegrees(Math.asin(value)) : Math.asin(value);
            case "acos" -> degrees ? Math.toDegrees(Math.acos(value)) : Math.acos(value);
            case "atan" -> degrees ? Math.toDegrees(Math.atan(value)) : Math.atan(value);
            case "ln" -> Math.log(value);
            case "log" -> Math.log10(value);
            case "sinh" -> Math.sinh(value);
            case "cosh" -> Math.cosh(value);
            case "tanh" -> Math.tanh(value);
            default -> throw new RuntimeException("Unknown expression: " + expr);
        };

        if (eat('+')) {
            return parseFactor();
        }
        if (eat('-')) {
            return -parseFactor();
        }
        double x;
        int startPos = pos;
        if (eat('(')) {
            x = parseExpression();
            eat(')');
        } else if ((ch >= '0' && ch <= '9') || ch == '.') {
            while ((ch >= '0' && ch <= '9') || ch == '.') {
                nextChar();
            }
            x = Double.parseDouble(expression.substring(startPos, pos));
        } else if (ch >= 'a' && ch <= 'z') {
            while (ch >= 'a' && ch <= 'z') {
                nextChar();
            }
            x = applyFunction.apply(expression.substring(startPos, pos), parseFactor());
        } else {
            throw new RuntimeException("Unknown character: " + (char)ch);
        }
        if (eat('^')) {
            x = Math.pow(x, parseFactor());
        }
        return x;
    }
}
