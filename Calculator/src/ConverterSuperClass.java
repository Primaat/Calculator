import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class ConverterSuperClass extends TopSuperClass {
    /**
     * An abstract child class of the TopSuperClass. This class adds fields and relevant methods to any of its child classes.
     * The child classes are Data, Dates, Energy, Length, Power, Pressure, Speed, Surface, Time and inner classes of Volume.
     */

    protected DecimalFormat cf = new DecimalFormat("#,##0.#####");

    //A string builder that contains the input from  any currently focused text field
    protected StringBuilder sb = new StringBuilder();

    // this is the standard value from which all other values are calculated, in the case of time, this will be thirdLeft
    protected double baseVal;

    // associates the amounts of the calculations to the fields
    protected double[] amounts;

    // the field from which all other fields are calculated. Use the index number from the string in the name array
    protected int baseField;

    // array for the names of the fields
    private String[] nameArray;

    // The text fields being used
    protected JTextField topLeft, topRight, secondLeft, secondRight, thirdLeft, thirdRight,
            fourthLeft, fourthRight, bottomLeft, bottomRight;

    // list of created JTextFields
    protected List<JTextField> fieldList;

    // the amount of fieldscreated
    protected int fieldsCreated;

    // the focused textfield name
    protected String currentFocus;

    // the previously focus textfield name
    protected String previousFocus;

    public ConverterSuperClass(String caller, Boolean actionListenerSetter, boolean buttonsOnly) {
        super(caller, actionListenerSetter, buttonsOnly);
        cf.setMaximumFractionDigits(5);
        cf.setMaximumIntegerDigits(15);

        //remove the original ActionListener set by the TopSuperClass
        for(JButton button: buttonArray){
            for(ActionListener al: button.getActionListeners()){
                button.removeActionListener(al);
            }
            button.addActionListener(new InputActionListener());
        }
    }

    public void run(String[] nameArray, int baseField ){
        // sets the array to the supplied array by the child class and runs the rest of the program
        this.nameArray = nameArray; // array of names for the JTextFields
        this.amounts = new double[nameArray.length]; // the amounts array should be as long as name array
        this.baseField = baseField; // the field from which all other fields are calculated
        fieldsCreator(); // creates the fields

        currentFocus = fieldList.get(baseField).getName(); // sets the focus on the field based on the baseField index
        previousFocus = currentFocus;
    }

    private void fieldsCreator(){
        /**
         * Creates the fields for the child classes
         */
        fieldsCreated = 0;
        FieldMouseListener tfml = new FieldMouseListener();
        JPanel fieldsPanel = new JPanel(new MigLayout("insets 0 10 0 0"));
        fieldsPanel.setBackground(ColorUIResource.getHSBColor(0.31f,0.09f,0.97f));

        topLeft = new JTextField();
        topRight = new JTextField();
        secondLeft = new JTextField();
        secondRight = new JTextField();
        thirdLeft = new JTextField();
        thirdRight = new JTextField();
        fourthLeft = new JTextField();
        fourthRight = new JTextField();
        bottomLeft = new JTextField();
        bottomRight = new JTextField();

        fieldList = new ArrayList<>();
        fieldList.add(topLeft);
        fieldList.add(topRight);
        fieldList.add(secondLeft);
        fieldList.add(secondRight);
        fieldList.add(thirdLeft);
        fieldList.add(thirdRight);
        fieldList.add(fourthLeft);
        fieldList.add(fourthRight);
        fieldList.add(bottomLeft);
        fieldList.add(bottomRight);

        for(int i = 0; i < nameArray.length; i++){
            fieldList.get(i).setText("0");
            fieldList.get(i).setName(nameArray[i]);
            fieldList.get(i).setEditable(false);
            fieldList.get(i).setBackground(Color.WHITE);
            fieldList.get(i).setMinimumSize(new Dimension(125, 25));
            fieldList.get(i).addMouseListener(tfml);
            fieldList.get(i).setBorder(BorderFactory.createEmptyBorder());

            JTextField txt = new JTextField();
            txt.setText(nameArray[i]);
            txt.setEditable(false);
            txt.setMinimumSize(new Dimension(65, 25));
            txt.setBorder(BorderFactory.createEmptyBorder());
            txt.setOpaque(false);
            txt.setFont(new Font("Arial", Font.BOLD, 10));

            if(fieldsCreated == 0){
                fieldsPanel.add(fieldList.get(i), "gap left 10, split 4, width 125, height 25, gapx 0, gapy 10");
                fieldsPanel.add(txt, "width 65, height 25");
            }
            else if(fieldsCreated == 1){
                fieldsPanel.add(fieldList.get(i), "width 125, height 25, gapx 0");
                fieldsPanel.add(txt, "wrap, width 65, height 25");
            }

            if(fieldList.get(i).getName().equals("Seconds")){
                fieldList.get(i).requestFocusInWindow();
            }

            if(fieldsCreated < 1){
                fieldsCreated++;
            }
            else{
                fieldsCreated = 0;
            }
        }

        this.add(fieldsPanel, "dock north");

        JTextField emptyText = new JTextField();
        emptyText.setBorder(BorderFactory.createEmptyBorder());
        emptyText.setOpaque(false);
        emptyText.setEditable(false);

        if(nameArray.length <= 8){
            JPanel emptyPanel = new JPanel();
            emptyPanel.setOpaque(false);
            emptyPanel.setSize(new Dimension(400, 150));
            emptyPanel.add(emptyText);
            this.add(emptyPanel, "dock north, width 408");
        }
    }

    private class FieldMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            JTextField source = (JTextField) e.getSource();
            source.requestFocusInWindow();
            previousFocus = currentFocus;
            currentFocus = source.getName();
        }
        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
    }

    private class InputActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            inputAction(command);
        }
    }

    protected void inputAction(String command){
        if(!currentFocus.equals(previousFocus)){
            sb.replace(0,sb.length(), "");
            previousFocus = currentFocus;
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
            if(sb.length() < 15){
                sb.append(command);
            }
        }
        if(!sb.toString().isEmpty()){
            Converter();
        }
    }

    public void comma(){
        if(!sb.toString().contains(".")) {
            sb.append(".");
        }
    }

    public void backspace(){
        if(sb.length() > 1){
            sb.delete(sb.length()-1,sb.length());
        }
        else{
            sb.replace(0,1 , "0");
        }
    }

    public void CE(){
        sb.replace(0, sb.length(), "");
        for(JTextField field: fieldList){
            field.setText("0");
        }
    }

    public abstract void Converter(); // child class should override. This method converts all other values to the base

    public abstract void fromToCalculation(); // child class should override. This method converts the base to all other values

    protected void setFields(){
        /**
         *  sets the fields to the converted amounts
         */
        for(JTextField field: fieldList){
            if(fieldList.indexOf(field) < nameArray.length) {
                field.setText(cf.format(new BigDecimal(amounts[fieldList.indexOf(field)])));
            }
        }
    }
}
