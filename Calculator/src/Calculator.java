import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Calculator extends JPanel{
    /**
     * The panel for the calculator
     */
    private ArrayList<String> operatorArray = new ArrayList<>();
    private ArrayList<String> specialOperatorArray = new ArrayList<>(); // for testing

    private DecimalFormat df = new DecimalFormat("#,##0.00");
    private String operatorInUse = "";
    private double total = 0;
    private double lastTotal = 0;
    private StringBuilder calculationString = new StringBuilder();
    private double previousInput = 0;
    private StringBuilder inputString = new StringBuilder();

    private StringBuilder softInputString = new StringBuilder();
    private String specialOperatorInUse = "";
    private double specialOperatorSubTotal = 0;

    private String firstInput = "";
    private String lastOperatorUsed = "";
    private String lastSpecialOperator = "";

    private Font charFont = new Font("Arial Black", Font.BOLD, 24 );

    private JPanel inputPanel = new JPanel(new MigLayout());

    private JTextField historyField = new JTextField();
    private JTextField inputResultField = new JTextField();
    private JButton[] jbuttonArray = new CalculationButtons().buttonArrayBuilder("Calculator");


    public Calculator(){
        ButtonActionListener bal = new ButtonActionListener();
        this.setPreferredSize(new Dimension(400,700));
        MigLayout buttonLayout = new MigLayout("insets 0, ","", " []0[]");
        this.setLayout(buttonLayout);
        this.setBackground(ColorUIResource.getHSBColor(0.31f,0.09f,0.97f));

        buttonLayout.setLayoutConstraints("al left, insets 0, gapx 0");
        operatorArray.add("+");
        operatorArray.add("-");
        operatorArray.add("/");
        operatorArray.add("x");
        specialOperatorArray.add("%");
        specialOperatorArray.add("X²");
        specialOperatorArray.add("√");
        specialOperatorArray.add("1/X");

        historyField.setAlignmentY(Component.CENTER_ALIGNMENT);
        historyField.setBorder(BorderFactory.createEmptyBorder());
        historyField.setBackground(Color.WHITE);
        historyField.setEditable(false);
        historyField.setFont(new Font("Arial", Font.PLAIN, 14));

        inputResultField.setAlignmentY(Component.CENTER_ALIGNMENT);
        inputResultField.setBorder(BorderFactory.createEmptyBorder());
        inputResultField.setBackground(Color.WHITE);
        inputResultField.setEditable(false);
        inputResultField.setFont(new Font("Arial Black", Font.BOLD, 24));
        inputResultField.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        this.add(historyField, "dock north, gapy 0, gapx 0, grow, wrap, width 404!, height 50!");
        this.add(inputResultField, "dock north, gapy 0, gapx 0, grow, width 404!, height 50!");


        int buttonWrapCounter = 1;
        for(JButton button: jbuttonArray){
            button.addActionListener(bal);
            if(buttonWrapCounter % 4 == 0){
                this.add(button, "gapy 1, gapx 1, wrap, width 100!, height 100!");
            }
            else if(buttonWrapCounter % 4 == 1){
                this.add(button, "split 4, gapy 1, gapx 1, grow, width 100!, height 100!");
            }
            else{
                this.add(button, "gapy 1, gapx 1, grow, width 100!, height 100!");
            }
            buttonWrapCounter++;
        }
        inputPanel.setPreferredSize(new Dimension(400,600));
        inputPanel.setBackground(ColorUIResource.getHSBColor(0.31f,0.09f,0.97f));
        inputPanel.setBorder(BorderFactory.createEmptyBorder());
        this.add(inputPanel, "dock center");
        init();
    }

    class ButtonActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if(command.equals("0")) {
                if(firstInput.equals("") && calculationString.toString().isEmpty() && inputString.toString().isEmpty()){
                    firstInput = "0";
                }
                if (!inputString.toString().equals("0")) {
                    inputString.append("0");
                }
            }
            else if(command.matches("\\d")){
                IntegerInput(command);
            }
            else if(command.equals("CE")) {
                inputString.replace(0, inputString.length(), "0");
            }
            else if(command.equals("C")) {
                firstInput = "";
                operatorInUse = "";
                lastOperatorUsed = "";
                lastSpecialOperator = "";
                inputString.replace(0, inputString.length(), "0");
                calculationString.delete(0,calculationString.length());
                lastTotal = 0.0;
                total = 0.0;
                specialOperatorInUse = "";
                softInputString.delete(0, softInputString.length());
                specialOperatorSubTotal = 0;
                previousInput = 0.0;
                displayOutputFields("0");
            }
            else if(command.equals("<--")) {
                if(inputString.length() == 1){
                    inputString.replace(0, inputString.length(), "0");                    }
                else {
                    inputString.delete(inputString.length() - 1, inputString.length());
                }
            }
            else if(command.equals(",")) {
                if(!inputString.toString().contains(".")) {
                    if (!inputString.toString().isEmpty()) {
                        inputString.append(".");
                    } else {
                        inputString.append("0" + ".");
                    }
                }
            }
            else if(command.equals("-/+")) {
                if(total != 0 && inputString.toString().isEmpty() || Double.parseDouble(inputString.toString()) == 0){
                    if(total > 0){
                        inputString.replace(0, inputString.length(), "" + java.lang.Math.abs(total));
                    }
                    else if(total < 0){
                        inputString.replace(0, inputString.length(), "" + (total * -1));
                    }
                }
                else if(Double.parseDouble(inputString.toString()) < 0){
                    inputString.replace(0, inputString.length(), "" + java.lang.Math.abs(Double.parseDouble(inputString.toString())));
                }
                else{
                    inputString.replace(0, inputString.length(), "" + Double.parseDouble(inputString.toString()) * -1);
                }
            }
            displayOutputFields(inputString.toString());

            String[] operatorArray = {"+","-","/","x","%","1/X","√","X²","="};
            List<String> operatorList = Arrays.asList(operatorArray);

            if(operatorList.contains(command)) {
                operatorHandler(command);
            }
        }
    }

    public void init(){
        if(inputString.toString().isEmpty()){
            displayOutputFields("0");
        }
    }

    public void IntegerInput(String integer){
        /**
         * This method checks if there is a single zero in the input string, if so it wil be replaced by an integer
         * else the integer will be added to the input string
         */
        operatorInUse = "";
        specialOperatorInUse = "";
        if(calculationString.toString().isEmpty() && (total >0 || total < 0)){
            if(lastTotal == 0){
                lastTotal = total;
                total = 0;
            }
            specialOperatorSubTotal = 0;
            softInputString.delete(0,softInputString.length());
        }
        else if(!softInputString.toString().isEmpty()){
            specialOperatorSubTotal = 0;
            softInputString.delete(0,softInputString.length());
        }

        if (inputString.toString().equals("0") || inputString.toString().equals("0.0")) {
            inputString.replace(0,1,integer);
        }
        else{
            inputString.append(integer);
        }
    }

    public void operatorHandler(String operator){
        String op = "";
        if(specialOperatorSubTotal != 0 && operatorArray.contains(operator)){
            /** if statement to make sure after special operators have been used, the result is processed
             *  correctly through the program afterwards
             */
            inputString.replace(0, inputString.length(), "" + specialOperatorSubTotal);
            specialOperatorSubTotal = 0;
        }
        if(operator.equals("=")){
            /**
             *  when the "=" operator is used, apply the resultCalculator method
             */
            resultCalculator();
        }
        else if(operatorArray.contains(operator)){
            lastSpecialOperator = "";
            if(calculationString.toString().isEmpty()){
                lastTotal = 0;
            }
            if(total != 0 && calculationString.toString().isEmpty()){
                lastOperatorUsed = "";
            }
            else if(inputString.toString().isEmpty() && !calculationString.toString().isEmpty()){
                if(calculationString.charAt(calculationString.length() -2) != operator.charAt(0)){
                    calculationString.replace(calculationString.length() - 2, calculationString.length()-1, operator);
                }
            }
            operatorInUse = operator;
            if(lastOperatorUsed.isEmpty() || !operatorArray.contains(lastOperatorUsed)){
                op = operatorInUse;
            }
            else{
                op = lastOperatorUsed;
            }
            switch(op){
                case "+":
                    plusCalculator();
                    break;
                case "-":
                    minusCalculator();
                    break;
                case "x":
                    multiplicationCalculator();
                    break;
                case "/":
                    divisionCalculator();
                    break;
                default:
                    break;
            }
        }
        else if(specialOperatorArray.contains(operator)){
            System.out.println("SpecialoperatorHandler - specialOperatorArray.contains(operator)");
            if(total != 0 && calculationString.toString().isEmpty()){
                //lastOperatorUsed = "";
            }
            specialOperatorInUse = operator;
            if(lastOperatorUsed.isEmpty() || !specialOperatorArray.contains(lastOperatorUsed)){
                op = specialOperatorInUse;
            }
            else{
                op = lastOperatorUsed;
            }
            switch(op){
                case "%":
                    percentageCalculator();
                    break;
                case "X²":
                    squaredCalculator();
                    break;
                case "√":
                    rootCalculator();
                    break;
                case "1/X":
                    fractionCalculator();
                    break;
                default:
                    break;
            }
        }
    }

    public void resultCalculator(){
        /**
         * Calculates a result of the calculation
         */
        if(calculationString.toString().isEmpty() && lastTotal != 0 && total != 0 && !inputString.toString().isEmpty() ){
            total = lastTotal;
            if(lastOperatorUsed.equals("+")){
                total += Double.parseDouble(inputString.toString());
            }
            else if(lastOperatorUsed.equals("-")){
                total -= Double.parseDouble(inputString.toString());
            }
            else if(lastOperatorUsed.equals("x")){
                total *= Double.parseDouble(inputString.toString());
            }
            else if(lastOperatorUsed.equals("/")){
                total /= Double.parseDouble(inputString.toString());
            }
            previousInput = total;
            inputString.delete(0,inputString.length());
        }
        else if(!inputString.toString().isEmpty() ){
            lastTotal = total;
            double number = Double.parseDouble(inputString.toString());
            if(lastOperatorUsed.equals("+")){
                previousInput = number;
                total += previousInput;
            }
            else if(lastOperatorUsed.equals("-")){
                previousInput = number;
                total -= previousInput;
            }
            else if(lastOperatorUsed.equals("x")){
                previousInput = number;
                total *= previousInput;
            }
            else if(lastOperatorUsed.equals("/")){
                previousInput = number;
                total /= previousInput;
            }
            else{
                total = number;
            }
            inputString.delete(0, inputString.length());
        }
        else if(specialOperatorSubTotal != 0 && previousInput != 0){
            if(lastOperatorUsed.equals("+")){
                previousInput = specialOperatorSubTotal;
                total = lastTotal + specialOperatorSubTotal;
            }
            else if(lastOperatorUsed.equals("-")){
                previousInput = specialOperatorSubTotal;
                total = lastTotal - specialOperatorSubTotal;
            }
            else if(lastOperatorUsed.equals("x")){
                previousInput = specialOperatorSubTotal;
                total = lastTotal * specialOperatorSubTotal;
            }
            else if(lastOperatorUsed.equals("/")){
                previousInput = specialOperatorSubTotal;
                total = lastTotal / specialOperatorSubTotal;
            }
            else{
                total = specialOperatorSubTotal;
            }
            specialOperatorSubTotal = 0;
        }
        else if(specialOperatorSubTotal != 0){
            if(lastOperatorUsed.equals("+")){
                previousInput = specialOperatorSubTotal;
                total += previousInput;
            }
            else if(lastOperatorUsed.equals("-")){
                previousInput = specialOperatorSubTotal;
                total -= previousInput;
            }
            else if(lastOperatorUsed.equals("x")){
                previousInput = specialOperatorSubTotal;
                total *= previousInput;
            }
            else if(lastOperatorUsed.equals("/")){
                previousInput = specialOperatorSubTotal;
                total /= previousInput;
            }
            else{
                total = specialOperatorSubTotal;
            }
            specialOperatorSubTotal = 0;
        }
        else if((previousInput < total || previousInput > total) && !calculationString.toString().isEmpty()){
            if(lastOperatorUsed.equals("+")){
                previousInput = total;
                total += previousInput;
            }
            else if(lastOperatorUsed.equals("-")){
                previousInput = total;
                total -= previousInput;
            }
            else if(lastOperatorUsed.equals("x")){
                previousInput = total;
                total *= previousInput;
            }
            else if(lastOperatorUsed.equals("/")){
                previousInput = total;
                total /= previousInput;
            }
        }
        else{
            if(specialOperatorSubTotal != 0){
                if(lastOperatorUsed.equals("+")){
                    total += specialOperatorSubTotal;
                }
                else if(lastOperatorUsed.equals("-")){
                    total -= specialOperatorSubTotal;
                }
                else if(lastOperatorUsed.equals("x")){
                    total *= specialOperatorSubTotal;
                }
                else if(lastOperatorUsed.equals("/")){
                    total /= specialOperatorSubTotal;
                }
            }
            else if(lastTotal != 0){
                if(lastOperatorUsed.equals("+")){
                    total += lastTotal;
                }
                else if(lastOperatorUsed.equals("-")){
                    total -= lastTotal;
                }
                else if(lastOperatorUsed.equals("x")){
                    total *= lastTotal;
                }
                else if(lastOperatorUsed.equals("/")){
                    total /= lastTotal;
                }
            }
            else{
                if(lastOperatorUsed.equals("+")){
                    total += previousInput;
                }
                else if(lastOperatorUsed.equals("-")){
                    total -= previousInput;
                }
                else if(lastOperatorUsed.equals("x")){
                    total *= previousInput;
                }
                else if(lastOperatorUsed.equals("/")){
                    total /= previousInput;
                }
            }
        }
        if(lastSpecialOperator.equals("%") && calculationString.toString().isEmpty() && specialOperatorSubTotal == 0){
            total /= 100;
        }
        if(lastTotal == 0){
            lastTotal = total;
        }
        calculationString.delete(0, calculationString.length());
        displayOutputFields("" + total);
    }

    public void plusCalculator(){
        /**
         * Adds to a calculation / calculated total
         */
        if(calculationString.toString().isEmpty() && (total > 0 || total < 0) && inputString.toString().isEmpty()){
            calculationString.insert(calculationString.length(), total + " " + operatorInUse + " ");
            previousInput = total;
        }
        else if(!inputString.toString().isEmpty()){
            double number = Double.parseDouble(inputString.toString());
            if(calculationString.toString().isEmpty()){
                total = 0;
            }
            if(!softInputString.toString().isEmpty()){
                calculationString.insert(calculationString.length(), softInputString.toString() + " " + operatorInUse + " ");
                softInputString.delete(0, softInputString.length());
            }
            else {
                calculationString.insert(calculationString.length(), inputString.toString() + " " + operatorInUse + " ");
            }
            total += number;
            previousInput = number;
            inputString.delete(0,inputString.length());
        }
        else if(specialOperatorSubTotal != 0){
            calculationString.insert(calculationString.length(), specialOperatorSubTotal + " " + operatorInUse + " ");
            total += specialOperatorSubTotal;
            previousInput = specialOperatorSubTotal;
            specialOperatorSubTotal = 0;
        }
        else{
            total += 0;
        }
        lastTotal = total;
        lastOperatorUsed = operatorInUse;
        displayOutputFields("" + total);
    }

    public void minusCalculator(){
        /**
         * Subtracts from a calculation / calculated total
         */
        if(calculationString.toString().isEmpty() && (total > 0 || total < 0) && inputString.toString().isEmpty()){
            calculationString.insert(calculationString.length(), total + " " + operatorInUse + " ");
            previousInput = total;
        }
        else if(!inputString.toString().isEmpty()){
            double number = Double.parseDouble(inputString.toString());
            if(calculationString.toString().isEmpty()){
                calculationString.insert(calculationString.length(), inputString.toString() + " " + operatorInUse + " ");
                total = number;
            }
            else if(!softInputString.toString().isEmpty()){
                calculationString.insert(calculationString.length(), softInputString.toString() + " " + operatorInUse + " ");
                softInputString.delete(0, softInputString.length());
                total -= number;
            }
            else {
                calculationString.insert(calculationString.length(), inputString.toString() + " " + operatorInUse + " ");
                total -= number;
            }
            previousInput = number;
            inputString.delete(0,inputString.length());
        }
        else if(specialOperatorSubTotal != 0){
            calculationString.insert(calculationString.length(), specialOperatorSubTotal + " " + operatorInUse + " ");
            total -= specialOperatorSubTotal;
            previousInput = specialOperatorSubTotal;
            specialOperatorSubTotal = 0;
        }
        else{
            total -= 0;
        }
        lastTotal = total;
        lastOperatorUsed = operatorInUse;
        displayOutputFields("" + total);
    }

    public void multiplicationCalculator(){
        /**
         * Multiplies a calculation / calculated total
         */
        if(calculationString.toString().isEmpty() && (total > 0 || total < 0)){
            calculationString.insert(calculationString.length(), total + " " + operatorInUse + " ");
            previousInput = total;
        }
        else if(!inputString.toString().isEmpty()){
            double number = Double.parseDouble(inputString.toString());
            if(total == 0){
                if((lastOperatorUsed.equals("/") || lastOperatorUsed.equals("*")) && firstInput.equals("0")){
                    total = 0;
                }
                else if(lastOperatorUsed.equals("+") || lastOperatorUsed.equals("-")){
                    total = number;
                }
                else if(calculationString.length() > 0 && calculationString.charAt(0) == '0'){
                    total = 0;
                }
                else{
                    total = number;
                }
            }
            else{
                total *= number;
            }
            if(!softInputString.toString().isEmpty()){
                calculationString.insert(calculationString.length(), softInputString.toString() + " " + operatorInUse + " ");
                softInputString.delete(0, softInputString.length());
            }
            else {
                calculationString.insert(calculationString.length(), inputString.toString() + " " + operatorInUse + " ");
            }
            if(firstInput.equals("0")){
                previousInput = 0;
            }
            else{
                previousInput = number;
            }
            inputString.delete(0,inputString.length());
        }
        else if(specialOperatorSubTotal != 0){
            calculationString.append(" * " + specialOperatorSubTotal + operatorInUse);
            total *= specialOperatorSubTotal;
            previousInput = specialOperatorSubTotal;
            specialOperatorSubTotal = 0;
        }
        else{
            total += 0;
        }
        lastTotal = total;
        lastOperatorUsed = operatorInUse;
        displayOutputFields("" + total);
    }

    public void divisionCalculator(){
        /**
         * Divides a calculation / calculated total
         */
        if(calculationString.toString().isEmpty() && (total > 0 || total < 0)){
            calculationString.insert(calculationString.length(), total + " " + operatorInUse + " ");
            previousInput = total;
        }
        else if(!inputString.toString().isEmpty()){
            double number = Double.parseDouble(inputString.toString());
            if(total == 0){
                if(lastOperatorUsed.equals("/") || lastOperatorUsed.equals("x")  && firstInput.equals("0")){
                    total = 0;
                }
                else if(lastOperatorUsed.equals("+") || lastOperatorUsed.equals("-")){
                    total = number;
                }
                else if(calculationString.length() > 0 && calculationString.charAt(0) == '0'){
                    total = 0;
                }
                else{
                    total = number;
                }
            }
            else{
                total /= number;
            }
            if(!softInputString.toString().isEmpty()){
                calculationString.insert(calculationString.length(), softInputString.toString() + " " + operatorInUse + " ");
                softInputString.delete(0, softInputString.length());
            }
            else {
                calculationString.insert(calculationString.length(), inputString.toString() + " " + operatorInUse + " ");
            }
            if(firstInput.equals("0")){
                previousInput = 0;
            }
            else{
                previousInput = number;
            }
            inputString.delete(0,inputString.length());
        }
        else if(specialOperatorSubTotal != 0){
            calculationString.insert(calculationString.length(), specialOperatorSubTotal + " " + operatorInUse + " ");
            total /= specialOperatorSubTotal;
            previousInput = specialOperatorSubTotal;
            specialOperatorSubTotal = 0;
        }
        else{
            total += 0;
        }
        lastTotal = total;
        lastOperatorUsed = operatorInUse;
        displayOutputFields("" + total);
    }

    public void percentageCalculator(){
        /**
         * Calculates the percentage of a number / calculated total
         */
        if((total > 0 || total < 0) && specialOperatorSubTotal == 0 && calculationString.toString().isEmpty()){
            if(!inputString.toString().isEmpty()){
                specialOperatorSubTotal = Double.parseDouble(inputString.toString()) / 100;
            }else{
                specialOperatorSubTotal = (total / 100) * total;
            }
        }
        else if((total > 0 || total < 0) && calculationString.toString().isEmpty()){
            specialOperatorSubTotal = (specialOperatorSubTotal / 100) * previousInput;
        }
        else if(lastOperatorUsed.equals("x") || lastOperatorUsed.equals("/")){
            if(!inputString.toString().isEmpty()){
                specialOperatorSubTotal = Double.parseDouble(inputString.toString()) / 100;
                inputString.delete(0, inputString.length());
            }
            else if(specialOperatorSubTotal > 0 || specialOperatorSubTotal < 0){
                specialOperatorSubTotal = specialOperatorSubTotal / 100;
            }
            else{
                specialOperatorSubTotal = total / 100;
            }
        }
        else if(lastOperatorUsed.equals("+") || lastOperatorUsed.equals("-")){
            if(!inputString.toString().isEmpty()){
                specialOperatorSubTotal = (Double.parseDouble(inputString.toString()) / 100) * total;
                inputString.delete(0, inputString.length());
            }
            else if(specialOperatorSubTotal > 0 || specialOperatorSubTotal < 0){
                specialOperatorSubTotal = (specialOperatorSubTotal / 100) * total;
            }
            else{
                specialOperatorSubTotal = (total / 100) * total;
            }
        }
        else if(!lastOperatorUsed.isEmpty() && !inputString.toString().isEmpty()){
            if(total != 0){
                specialOperatorSubTotal = (Double.parseDouble(inputString.toString()) / 100) * total;

            }else{
                specialOperatorSubTotal = Double.parseDouble(inputString.toString()) / 100;
            }
            inputString.delete(0, inputString.length());
        }
        if(calculationString.toString().isEmpty() && total == 0){
            inputString.replace(0, inputString.length(), "" + 0);
            displayOutputFields("" + specialOperatorSubTotal, "" + 0);
        }
        else{
            displayOutputFields("" + specialOperatorSubTotal, "" + specialOperatorSubTotal);
        }
        lastSpecialOperator = specialOperatorInUse;
    }

    public void squaredCalculator(){
        /**
         * Calculates the square of a number / calculated total
         */
        if(!inputString.toString().isEmpty()){
            specialOperatorSubTotal = Double.parseDouble(inputString.toString()) * Double.parseDouble(inputString.toString());
            softInputString.replace(0,softInputString.length(), "sqr(" + inputString.toString() + ")");
            inputString.delete(0, inputString.length());
        }
        else if(specialOperatorSubTotal != 0){
            specialOperatorSubTotal = specialOperatorSubTotal * specialOperatorSubTotal;
            softInputString.replace(0,softInputString.length(), "sqr(" + softInputString + ")");
        }
        else {
            specialOperatorSubTotal = total * total;
            softInputString.replace(0,softInputString.length(), "sqr(" + total + ")");
        }
        lastSpecialOperator = specialOperatorInUse;
        displayOutputFields("" + specialOperatorSubTotal, softInputString.toString());
    }

    public void rootCalculator(){
        /**
         * Calculates the root of a number / calculated total
         */
        if(!inputString.toString().isEmpty()){
            specialOperatorSubTotal = java.lang.Math.sqrt(Double.parseDouble(inputString.toString()));
            softInputString.replace(0,softInputString.length(), "√(" + inputString.toString() + ")");
            inputString.delete(0, inputString.length());
        }
        else if(specialOperatorSubTotal != 0){
            specialOperatorSubTotal = java.lang.Math.sqrt(specialOperatorSubTotal);
            softInputString.replace(0,softInputString.length(), "√(" + softInputString + ")");
        }
        else {
            specialOperatorSubTotal = java.lang.Math.sqrt(total);
            softInputString.replace(0,softInputString.length(), "√(" + total + ")");
        }
        lastSpecialOperator = specialOperatorInUse;
        displayOutputFields("" + specialOperatorSubTotal, softInputString.toString());
    }

    public void fractionCalculator(){
        /**
         * Calculates the fraction of a number / calculated total
         */
        if(!inputString.toString().isEmpty()){
            specialOperatorSubTotal =  1 / Double.parseDouble(inputString.toString());
            softInputString.replace(0,softInputString.length(), "1/(" + inputString.toString() + ")");
            inputString.delete(0, inputString.length());
        }
        else if(specialOperatorSubTotal != 0){
            specialOperatorSubTotal = 1 / specialOperatorSubTotal;
            softInputString.replace(0,softInputString.length(), "1/(" + softInputString + ")");
        }
        else {
            specialOperatorSubTotal = 1 / total;
            softInputString.replace(0,softInputString.length(), "1/(" + total + ")");
        }
        lastSpecialOperator = specialOperatorInUse;
        displayOutputFields("" + specialOperatorSubTotal, softInputString.toString());
    }

    public void displayOutputFields(String resultOutput){
        /**
         *  This method shows the history string, input string and / or total in the GUI
         */
        historyField.setText(calculationString.toString());
        inputResultField.setText(resultOutput);

    }

    public void displayOutputFields(String resultOutput, String softInputString){
        /**
         *  This method shows the history string, input string and / or total in the GUI
         */
        historyField.setText(calculationString.toString() + "" + softInputString );
        inputResultField.setText(resultOutput);
    }
}
