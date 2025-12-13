## Scientific Calculator
A robust, desktop-based scientific calculator built entirely in Java 21. This application features a modern dark-mode user interface and a custom-built mathematical evaluation engine, removing the need for deprecated JavaScript engines or external libraries.

# Features
- Pure Java 21: Zero external dependencies. Runs on the standard JDK.
- Custom Math Parser: Implements a Recursive Descent Parser to evaluate complex mathematical expressions safely and efficiently.
- Scientific Functions:
1. Trigonometry: sin, cos, tan, asin, acos, atan (Supports Degrees and Radians).
2. Hyperbolic: sinh, cosh, tanh.
3. Logarithms: log (base 10), ln (natural log).
4. Powers & Roots: ^, sqrt.
5. Factorial: ! (Handles large number calculations).
6. Memory Functions: MC, MR, M+, M-.
- History Panel: Scrollable history of past calculations.
- Modern UI: Dark theme with color-coded buttons and cross-platform compatibility.

# Technology Stack
- Language: Java
- GUI Framework: Java Swing / AWT
- Algorithm: Recursive Descent Parser (replacing the deprecated ScriptEngine / Nashorn).

# Installation & Setup
Prerequisites:
- Java Development Kit (JDK) 21 or higher.
- IntelliJ IDEA (recommended) or any Java IDE.

How to run:
1. Clone the repository:
   ```bash
   git clone https://github.com/lucadani7/ScientificCalculator.git
   ```
2. Open in IntelliJ IDEA:
   - File -> Open -> Select the project folder.
   - Ensure Project SDK is set to 21.
3. Run:
   - Navigate to src/com/lucadani/Main.java.
   - Click the Green Play button (Run).
  
# How It Works (The Parser)
- Unlike older Java calculators that relied on the now-removed ScriptEngine (JavaScript), this project uses a Recursive Descent Parser.
- The MathEvaluator class breaks down expressions into tokens and evaluates them based on operator precedence:
- Factor: Numbers, Parentheses, Functions (sin, sqrt).
- Term: Multiplication, Division, Modulo.
- Expression: Addition, Subtraction.
- This ensures that an expression like 2 + 3 * 4 is correctly evaluated as 14, not 20.

# Usage
- Toggle Modes: Use the Deg and Rad buttons to switch between Degree and Radian modes for trigonometric calculations.
- Scientific Notation: The calculator supports large numbers (e.g., Factorials) and displays them in scientific notation (e.g., 8.06E67).
- Keyboard Support: You can type numbers and operators directly from your keyboard.

## License
Distributed under the **Apache 2.0 License**. See `LICENSE` file for more information.
