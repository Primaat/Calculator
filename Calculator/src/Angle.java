import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Angle extends TopSuperClass{
    /**
     * panel for the angle conversion program. Uses the layout provided by TopSuperClass
     */
    private DecimalFormat df = new DecimalFormat("#,##0.##########");
    String[] array = {"Degree","Radian","Gradient"};
    DefaultComboBoxModel model1 = new DefaultComboBoxModel(array);
    DefaultComboBoxModel model2 = new DefaultComboBoxModel(array);

    public Angle() {
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

            if(input == 0){
                toField.setText("0");
                fromField.setText("0");
            }
            else {
                if(currentFocus.equals("to")){
                    inputBox = toBox;
                    outputBox = fromBox;
                }
                switch(inputBox){
                    case "Degree":
                        switch(outputBox){
                            case "Degree":
                                output = input;
                                break;
                            case "Radian":
                                output = input * 0.0174532925199433;
                                break;
                            case "Gradient":
                                output = input * 1.1111111111111;
                                break;
                        }
                        break;
                    case "Radian":
                        switch(outputBox){
                            case "Degree":
                                output = input * 57.2957795130823;
                                break;
                            case "Radian":
                                output = input;
                                break;
                            case "Gradient":
                                output = input * 63.6619772367581;
                                break;
                        }
                        break;
                    case "Gradient":
                        switch(outputBox){
                            case "Degree":
                                output = input * 0.9;
                                break;
                            case "Radian":
                                output = input * 0.015707963267949;
                                break;
                            case "Gradient":
                                output = input;
                                break;
                        }
                        break;
                }
                BigDecimal bd = BigDecimal.valueOf(output);
                if(currentFocus.equals("from")){
                    toField.setText("" + df.format(bd));
                }
                else{
                    fromField.setText("" + df.format(bd));
                }
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
        /**
         * Method handling comma input in the text fields
         */
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
        /**
         * Method handling backspace input in the text fields
         */
        if(sb.length() == 1){
            sb.replace(0, sb.length(), "");
        }
        else if(sb.length() <  0){
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
