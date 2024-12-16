/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author ckat
 */
import java.util.Stack;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CassiopeiaCalculator{
    
    // So StringBuilder is used to ensure mutability of the input string, which happens to be the input expression
    // Stack is used to organize things in order, numbers and then operators (this specific order is important!)
    // Important methods of Stack are peek(), push() and pop() 
    // Peek is just looking at what is at the top of the stack, push adds to the top of the stack and pop removes from the top of the stack
    // Stack and StringBuilder is what makes this all possible <3
    
    public static double evaluate(String expression){
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();
        
        // A little error handling
        if(expression.isEmpty() || !(Character.isDigit(expression.charAt(0)) || expression.charAt(0) == '(')) 
        {throw new IllegalArgumentException("Expression must start with a digit or '('");}

        // Start breaking down the expression from left to right, separating numbers and operators
        for(int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if(Character.isDigit(ch)) {
                StringBuilder num = new StringBuilder();
                // So store ('push' into the stack) the number into a numbers only stack, also taking into consideration numbers with decimals
                // Use parsing to change from string to double
                while(i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) 
                {
                    num.append(expression.charAt(i++));
                }
                i--;
                numbers.push(Double.parseDouble(num.toString()));
            } 
            // Now we need to store the operators too - if the operator ends with a closing paranthesis, we evaluate what is inside that parenthesis (method evaluateTop())
            // Peek looks at what is in the stack without removing it, so in this way we can see if we need to evaluate or not, we eventually pop the '('
            else if (ch == '(') {
                operators.push(ch);
            } else if (ch == ')') {
                while (operators.peek() != '(') {
                    evaluateTop(numbers, operators);
                }
                operators.pop(); 
            } 
            // By checking that there is precedence, we evaluate and put back in the stacks simplified numbers when a precedence occurs 
            // ie 3+2*5 changes the stack from num [3][2][5] & op [+][*] to num [3][10] & op [+] to num [13] & op []
            else if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^') {
                while (!operators.isEmpty() && hasPrecedence(ch, operators.peek())) {
                    evaluateTop(numbers, operators);
                }
                operators.push(ch);
            }
        }
        // Keep evaluating until we run out of operators
        while (!operators.isEmpty()) {
            evaluateTop(numbers, operators);
        }

        return numbers.pop();
    }
    
    //This is where the math happens - pop the numbers and operators out of the stack 
    private static void evaluateTop(Stack<Double> numbers, Stack<Character> operators) {
        char operator = operators.pop();
        double b = numbers.pop();
        double a = numbers.pop();

        switch (operator) {
            case '+':
                numbers.push(a + b);
                break;
            case '-':
                numbers.push(a - b);
                break;
            case '*':
                numbers.push(a * b);
                break;
            case '/':
                if(b==0){
                    throw new ArithmeticException("Division by Zero has occured in the calculation"); // More error handling
                }
                numbers.push(a / b);
                break;
            case '^':
                numbers.push(Math.pow(a,b));
                break;
        }
    }
    
    // Ensure that PEMDAS is followed, in the operator stack
    private static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        // So if ^ is the operator, it can also have a ^ to the right of it  such as 3 ^ 3 ^ 3
        if (op1 == '^' && op2 != '^') {
            return false;
        }
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return false;
        }
        return true;
    }
    
    // It all comes together, user input and calculator output
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Please only use the following operators:\n (\n )\n ^\n *\n /\n +\n -");
        System.out.println("Type 'exit' to end the program.");
        
        while(true){
            System.out.println("Please input expression: "); 
            String expression = input.nextLine(); // This is the user input of the expression to be evaluated
            
            if(expression.equalsIgnoreCase("exit")){
                System.out.println("Goodbye");
                break;
            }
            
            try {
                System.out.println(evaluate(expression));// Output of the solution
            }
            catch(IllegalArgumentException | ArithmeticException e) {
                System.out.println("Error: " + e.getMessage());
            }
            
        }
        
    }
    /* Originally was going to expand for buttons, but it was giving me a blank so I decided against it due to time constraints
    public static void main(String[] args){
        JFrame frame = new JFrame("Cassiopeia's Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(250,300);
        JTextField inputField = new JTextField(20);
        JButton calculateButton = new JButton("=");
        JLabel resultLabel = new JLabel("Result: ");
        JButton exitButton = new JButton("Exit");
        
        frame.setLayout(new FlowLayout());
        
        frame.add(new JLabel("Enter Expression: "));
        frame.add(inputField);
        frame.add(calculateButton);
        frame.add(resultLabel);
        frame.add(exitButton);
        
        calculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                String expression = inputField.getText();
                try{
                    double result = evaluate(expression);
                    resultLabel.setText("Result: "+result);
                }
                catch(IllegalArgumentException | ArithmeticException ex){
                    resultLabel.setText("Error: "+ex.getMessage());
                }
            }
        });             
    }*/
}