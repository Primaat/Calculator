import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Temperature extends TopSuperClass {
    /**
     *  panel for the temperature conversion program
     */
    private DecimalFormat df = new DecimalFormat("#,##0.##########");
    String[] array = {"Celcius","Fahrenheit","Kelvin"};
    DefaultComboBoxModel model1 = new DefaultComboBoxModel(array);
    DefaultComboBoxModel model2 = new DefaultComboBoxModel(array);

    public Temperature() {
        super("Standard", false, false);
        this.fromBox.setModel(model1);
        this.fromBox.addActionListener(new BoxActionListener());
        this.fromSymbol.setText(fromBox.getSelectedItem().toString().substring(0, 1));

        this.toBox.setModel(model2);
        this.toBox.addActionListener(new BoxActionListener());
        this.toSymbol.setText(fromBox.getSelectedItem().toString().substring(0, 1));

        for(JButton button: buttonArray){
            for(ActionListener al: button.getActionListeners()){
                button.removeActionListener(al);
            }
            button.addActionListener(new InputActionListener());
        }
        Converter();
    }

    class BoxActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox scb = (JComboBox)e.getSource();
            String command = e.getActionCommand();
            int currencyIndex = scb.getSelectedIndex();
            scb.requestFocus();
            scb.setSelectedIndex(currencyIndex);
            if(command.equals("from")){
                fromSymbol.setText(scb.getSelectedItem().toString().substring(0, 1));
            }
            else{
                toSymbol.setText(scb.getSelectedItem().toString().substring(0, 1));
            }
            sb.replace(0, sb.length(), "0");
            Converter();
        }
    }

    @Override
    protected void Converter() {
        if(sb.length() > 0) {
            input = Double.parseDouble(sb.toString());

            String fromBox = this.fromBox.getSelectedItem().toString();
            String toBox = this.toBox.getSelectedItem().toString();

            String inputBox = fromBox;
            String outputBox = toBox;

            if(currentFocus.equals("to")){
                inputBox = toBox;
                outputBox = fromBox;
            }
            switch(inputBox){
                case "Celcius":
                    switch(outputBox){
                        case "Celcius":
                            output = input;
                            break;
                        case "Fahrenheit":
                            output = input * (9.0/5) + 32;
                            break;
                        case "Kelvin":
                            output = input + 273.15;
                            break;
                    }
                    break;
                case "Fahrenheit":
                    switch(outputBox){
                        case "Celcius":
                            output = (input - 32) * (5.0/9);
                            break;
                        case "Fahrenheit":
                            output = input;
                            break;
                        case "Kelvin":
                            output = (input + 459.67) * (5.0/9);
                            break;
                    }
                    break;
                case "Kelvin":
                    switch(outputBox){
                        case "Celcius":
                            output = input - 273.15;
                            break;
                        case "Fahrenheit":
                            output = input * (9/5) - 459.67;
                            break;
                        case "Kelvin":
                            output = input;
                            break;
                    }
                    break;
            }
            BigDecimal bd = BigDecimal.valueOf(output); //.setScale(0, RoundingMode.HALF_UP);
            if(currentFocus.equals("from")){
                toField.setText("" + df.format(bd));
            }
            else{
                fromField.setText("" + df.format(bd));
            }
        }
    }

    class InputActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            inputAction(command);
        }
    }

    public void inputAction(String command){

        if((currentFocus.equals("from") && !toString.isEmpty()) ||
                (currentFocus.equals("to") && !fromString.isEmpty())){
            toString = "";
            fromString = "";
            sb.replace(0,sb.length(), "");
        }
        if(command.equals(",")){
            comma();
        }
        else if(command.equals("<--")) {
            backspace();
        }
        else if(command.equals("CE")) {
            CE();
        }
        else{
            if((currentFocus.equals("from") && fromString.isEmpty()) ||
                    (currentFocus.equals("to") && toString.isEmpty())){
                toString = "";
                fromString = "";
                sb.replace(0,sb.length(), command);
            }
            else if(sb.length() < 16){
                sb.append(command);
            }

            if(currentFocus.equals("from")){
                fromString = sb.toString();
                fromField.setText(df.format(new BigDecimal(sb.toString())));
            }
            else{
                toString = sb.toString();
                toField.setText(df.format(new BigDecimal(sb.toString())));
            }
        }
        Converter();
    }


    public void comma(){
        if(!sb.toString().contains(".")) {
            sb.append(".");
        }
        if(currentFocus.equals("from")){
            fromField.setText(sb.toString());
        }
        else{
            toField.setText(sb.toString());
        }

    }
    public void backspace(){
        if(sb.length() == 1){
            sb.replace(0, sb.length(), "");
        }
        else if(sb.length() > 0){
            sb.delete(sb.length()-1,sb.length());
        }

        if(currentFocus.equals("from")){
            if(sb.length() > 0) {
                fromField.setText(sb.toString());
            }
            else{
                fromField.setText("0");
                toField.setText("0");
            }
        }
        else{
            if(sb.length() > 0) {
                toField.setText(sb.toString());
            }
            else{
                toField.setText("0");
                fromField.setText("0");
            }
        }
    }
}
