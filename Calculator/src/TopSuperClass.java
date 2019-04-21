import com.google.gson.Gson;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;


public class TopSuperClass extends JPanel {
    /**
     * The Class that is the basis for all panels using the same layout
     * this includes the Angle, Crypto, Currency, Temperature
     */

    private DecimalFormat df = new DecimalFormat("#,##0.00");
    // On which field is
    public String currentFocus = "from";
    // records how many decimals are being used in the focused input field
    public int decimalsUsed = 0;

    public String fromString = "";
    public String toString = "";

    public StringBuilder sb = new StringBuilder();

    public JTextField fromSymbol;
    public JTextField fromField;
    public JComboBox fromBox;
    private JPanel fromPanel;

    private DefaultComboBoxModel fromCurrencyModel;
    private List<String> shortNames =  new ArrayList<>();

    private DefaultComboBoxModel toCurrencyModel;
    private List<String> longNames = new ArrayList<>();

    private List<String> combinedNames = new ArrayList<>();
    private List<Double> currenciesBase = new ArrayList<>();
    private Map<String, Double> currenciesBasePairs = new TreeMap<>();

    public JTextField toSymbol;
    public JTextField toField;
    public JComboBox toBox;
    private JPanel toPanel;

    private JTextField curToCurField;

    private JPanel inputPanel;

    private InputButtonActionListener ibal;
    public JButton[] buttonArray;

    // Is an action listener set to the combo boxes?
    private boolean setActionListener;

    // Are only buttons needed? if no, the panel also includes fields and boxes
    private boolean buttonsOnly;

    // The panel calling the
    private String caller;

    private String baseSymbol;

    // vars set by the currency converter
    DecimalFormat cf = new DecimalFormat("#,##0.0000");
    double input= 0;
    double fromBase = 0;
    double base = 0;
    double toBase = 0;
    double output = 0;

    public TopSuperClass(String caller, Boolean actionListenerSetter, boolean buttonsOnly){
        // panel without combo box listeners
        this.caller = caller;
        this.setActionListener = actionListenerSetter;
        this.buttonsOnly = buttonsOnly;
        layoutBuilder();
        if(buttonsOnly){
            buttons();
        }
        else{
            fieldsAndBoxes();
            buttons();
        }
    }

    private void layoutBuilder(){
        MigLayout buttonLayout = new MigLayout("insets 0, ","", " []0[]");
        buttonLayout.setLayoutConstraints("al center, insets 0, gapx 0, gapy 0");
        this.setLayout(buttonLayout);
        this.setBackground(ColorUIResource.getHSBColor(0.31f,0.09f,0.97f));
    }

    public void fieldsAndBoxes(){
        AmountFieldMouseListener afal = new AmountFieldMouseListener();

        fromSymbol = new JTextField();
        fromSymbol.setBorder(BorderFactory.createEmptyBorder());
        fromSymbol.setOpaque(false);
        fromSymbol.setEditable(false);
        fromSymbol.setFont(new Font("Arial", Font.PLAIN, 16));

        fromField = new JTextField();
        fromField.setName("fromField");
        fromField.addMouseListener(afal);
        fromField.setColumns(16);
        fromField.setBorder(BorderFactory.createEmptyBorder());
        fromField.setOpaque(false);
        fromField.setText("0");
        fromField.setEditable(false);
        fromField.setFont(new Font("Arial Black", Font.BOLD, 20));
        fromField.setFocusable(true);
        fromField.requestFocus();

        // create an array to fill the fromBox with from the loaded name data through currencyRequest() method

        fromBox = new JComboBox();
        fromBox.setMaximumSize(new Dimension(200, 30 ));
        fromBox.setBackground(Color.WHITE);
        fromBox.setActionCommand("from");
        fromBox.setSelectedIndex(shortNames.indexOf("EUR"));
        fromBox.setMaximumRowCount(20);
        fromBox.setFont(new Font("Arial Black", Font.BOLD, 14));
        fromBox.setMaximumRowCount(9);

        fromPanel = new JPanel(new MigLayout());
        fromPanel.setOpaque(false);
        fromPanel.setSize(new Dimension(400,110));
        fromPanel.add(fromSymbol, "gapy 20, split 2, width 70, height 40");
        fromPanel.add(fromField, "align left, wrap, height 40");
        fromPanel.add(fromBox, "width 200, height 30");

        toSymbol = new JTextField();
        toSymbol.setBorder(BorderFactory.createEmptyBorder());
        toSymbol.setOpaque(false);
        toSymbol.setEditable(false);
        toSymbol.setFont(new Font("Arial", Font.PLAIN, 16));

        toField = new JTextField();
        toField.setName("toField");
        toField.addMouseListener(afal);
        toField.setColumns(16);
        toField.setBorder(BorderFactory.createEmptyBorder());
        toField.setOpaque(false);
        toField.setText("0");
        toField.setEditable(false);
        toField.setFocusable(true);
        toField.setFont(new Font("Arial Black", Font.BOLD, 20));

        // create an array to fill the fromBox with from the loaded name data through currencyRequest() method

        toBox = new JComboBox();
        toBox.setMaximumSize(new Dimension(200, 30 ));
        toBox.setBackground(Color.WHITE);
        toBox.setSelectedIndex(shortNames.indexOf("EUR"));
        toBox.setActionCommand("to");
        toBox.setMaximumRowCount(20);
        toBox.setFont(new Font("Arial Black", Font.BOLD, 14));
        toBox.setEditable(false);
        toBox.setMaximumRowCount(9);

        curToCurField = new JTextField();
        curToCurField.setColumns(40);
        curToCurField.setEditable(false);
        curToCurField.setBorder(BorderFactory.createEmptyBorder());
        curToCurField.setOpaque(false);
        curToCurField.setFont(new Font("Arial", Font.PLAIN, 12));

        toPanel = new JPanel(new MigLayout());
        toPanel.setOpaque(false);
        toPanel.setSize(new Dimension(400,110));
        toPanel.add(toSymbol, "gapy 20, split 2, width 70, height 40");
        toPanel.add(toField, "align left, wrap, height 40");
        toPanel.add(toBox, "wrap, width 200, height 30");
        toPanel.add(curToCurField, "height 20");

        this.add(fromPanel, "dock north, wrap, height 30");
        this.add(toPanel, "dock north, height 30");
    }

    public void buttons(){
        buttonArray = new CalculationButtons().buttonArrayBuilder(caller);

        ibal = new InputButtonActionListener();
        int buttonWrapCounter = 1;
        for(JButton button: buttonArray){

            if(button.getText().equals("")){
                button.setVisible(false);
            }

            button.addActionListener(ibal);

            if(buttonWrapCounter % 3 == 0){
                this.add(button, "gapy 1, gapx 1, wrap, width 100!, height 100!");
            }
            else if(buttonWrapCounter % 3 == 1){
                this.add(button, "al center, split 3, gapy 1, gapx1, grow, width 100!, height 100!");
            }
            else{
                this.add(button, "gapy 1, gapx 1, grow, width 100!, height 100!");
            }
            buttonWrapCounter++;
        }
        // if Bool actionListenerSetter is true, activate the method with the same name
        if(setActionListener){
            actionListenerSetter();
        }

        inputPanel = new JPanel(new MigLayout());
        inputPanel.setOpaque(false);
        inputPanel.setBorder(BorderFactory.createEmptyBorder());

        this.add(inputPanel, "dock north, width 410");
    }

    public void actionListenerSetter(){
        ComboBoxActionListener fromCbal = new ComboBoxActionListener();
        ComboBoxActionListener toCbal = new ComboBoxActionListener();

        fromBox.addActionListener(fromCbal);
        toBox.addActionListener(toCbal);
    }

    class InputButtonActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            inputAction(command);
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
            else if(sb.length() > 3 && sb.substring(sb.length()-3).equals(".00") && decimalsUsed == 0){
                sb.replace(sb.length()-2,sb.length()-1, command);
                decimalsUsed++;
            }
            else if(decimalsUsed == 1){
                sb.replace(sb.length()-1,sb.length(), command);
                decimalsUsed++;
            }
            else if(decimalsUsed == 2){
                // no more decimals can be used until backspace or CE are used
            }
            else if(decimalsUsed == 0 && sb.length() < 15){
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
    }

    public void comma(){
        if(!sb.toString().contains(".")) {
            if (!sb.toString().isEmpty()) {
                sb.append(".00");
            } else {
                sb.append("0" + ".00");
            }
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
        else if(decimalsUsed == 2){
            sb.replace(sb.length()-1,sb.length(), "0");
        }
        else if(decimalsUsed == 1){
            sb.replace(sb.length()-2,sb.length()-1, "0" );
        }
        else if(sb.length() > 3 && sb.substring(sb.length()-3).equals(".00")){
            sb.delete(sb.length()-3, sb.length());
        }
        else if(sb.length() > 0){
            sb.delete(sb.length()-1,sb.length());
        }

        if(decimalsUsed > 0){
            decimalsUsed--;
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

    public void CE(){
        sb.replace(0, sb.length(), "");
        fromField.setText("0");
        toField.setText("0");
        decimalsUsed = 0;
    }

    class AmountFieldMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            JTextField source = (JTextField) e.getSource();
            String name = source.getName();
            System.out.println(source.getName());
            switch(name){
                case "fromField":
                    fromField.requestFocusInWindow();
                    currentFocus = "from";
                    break;
                case "toField":
                    toField.requestFocusInWindow();
                    currentFocus = "to";
                    break;
            }
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

    class ComboBoxActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox scb = (JComboBox)e.getSource();
            String command = e.getActionCommand();
            int currencyIndex = scb.getSelectedIndex();
            scb.requestFocus();
            scb.setSelectedIndex(currencyIndex);
            if(command.equals("from")){
                fromSymbol.setText(shortNames.get(currencyIndex));
                curToCurField.setText("1 " + fromSymbol.getText() + " = " + cf.format((base * toBase) / input) + " " + toSymbol.getText());
            }
            else{
                toSymbol.setText(shortNames.get(currencyIndex));
                curToCurField.setText("1 " + toSymbol.getText() + " = " + cf.format((base * fromBase) / input) + " " + fromSymbol.getText());
            }
            //Converter();
        }
    }

    protected void Converter(){
        /**
         * converts any amount from the from box to the to box or vice versa
         */
        if(sb.length() > 0) {
            input = Double.parseDouble(sb.toString());
            fromBase = currenciesBasePairs.get(fromSymbol.getText());
            base = 0;
            toBase = currenciesBasePairs.get(toSymbol.getText());
            output = 0;
            if(input == 0){
                toField.setText("0");
                fromField.setText("0");
            }
            else if ( currentFocus.equals("from") ) {
                base = input / fromBase;
                output = base * toBase;
                BigDecimal bd = BigDecimal.valueOf(output).setScale(2, RoundingMode.HALF_UP);
                toField.setText("" + df.format(bd));
                curToCurField.setText("1 " + fromSymbol.getText() + " = " + cf.format((base * toBase) / input) + " " + toSymbol.getText());
            } else {
                base = input / toBase;
                output = base * fromBase;
                BigDecimal bd = BigDecimal.valueOf(output);
                fromField.setText("" + df.format(bd));
                curToCurField.setText("1 " + toSymbol.getText() + " = " + cf.format((base * fromBase) / input) + " " + fromSymbol.getText());
            }
        }
    }

    public void getSymbols(String urlLink, String baseSymbol){
        /**
         * get the currencies and symbols from a JSON file through a url and set them to maps and lists
         */
        this.baseSymbol = baseSymbol;
        try {
            URL url = new URL(urlLink);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            JsonSymbols result = new Gson().fromJson(reader, JsonSymbols.class);

            shortNames.addAll(result.getSymbols().keySet());
            longNames.addAll(result.getSymbols().values());

            for(int i = 0; i < shortNames.size(); i++){
                combinedNames.add(shortNames.get(i) + " - " + longNames.get(i));
            }
            fromCurrencyModel = new DefaultComboBoxModel(combinedNames.toArray());
            fromBox.setModel(fromCurrencyModel);
            toCurrencyModel = new DefaultComboBoxModel(combinedNames.toArray());
            toBox.setModel(toCurrencyModel);

            fromBox.setSelectedIndex(shortNames.indexOf(baseSymbol));
            toBox.setSelectedIndex(shortNames.indexOf(baseSymbol));

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void getRates(String urlLink){
        /**
         * get the latest currency rates with EUR as the base currency
         */
        try {
            URL url = new URL(urlLink);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            JsonCurrency result2 = new Gson().fromJson(reader, JsonCurrency.class);
            currenciesBasePairs = result2.rates;
            currenciesBase.addAll(result2.rates.values());
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void getCryptoSymbols(String symbolsURL, String ratesURL, String baseSymbol){
        /**
         * get the symbols and long names of crypto currencies, put them in an array of objects
         * and spread the objects over 3 lists after which combobox models are being created.
         */

        this.baseSymbol = baseSymbol;
        try {
            URL url = new URL(symbolsURL);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            JsonCryptoSymbols[] array = new Gson().fromJson(reader, JsonCryptoSymbols[].class);

            for(JsonCryptoSymbols Object: array){
                shortNames.add(Object.asset_id);
                longNames.add(Object.name);
                combinedNames.add(Object.asset_id + " - " + Object.name);
            }
            Collections.sort(combinedNames);
            Collections.sort(shortNames);

            getCryptoRates(ratesURL);

            fromCurrencyModel = new DefaultComboBoxModel(combinedNames.toArray());
            fromBox.setModel(fromCurrencyModel);
            toCurrencyModel = new DefaultComboBoxModel(combinedNames.toArray());
            toBox.setModel(toCurrencyModel);

            fromBox.setSelectedIndex(shortNames.indexOf(baseSymbol));
            toBox.setSelectedIndex(shortNames.indexOf(baseSymbol));

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void getCryptoRates(String ratesURL) {
        /**
         *         get the latest crypto rates with BTC as the base currency and fill a Tree map with name - rate pairs
         */
        try {
            URL url = new URL(ratesURL);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            JsonCrypto result = new Gson().fromJson(reader, JsonCrypto.class);
            JsonCryptoRates[] array = result.rates;

            currenciesBasePairs.put("BTC", 1.0);
            for(JsonCryptoRates Object: array){
                    currenciesBasePairs.put(Object.asset_id_quote, Object.rate);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    class JsonSymbols {
        /**
         * used to parse a JSON file with currency symbols with
         */
        String succes;
        LinkedHashMap<String, String> symbols;

        public LinkedHashMap<String, String> getSymbols() {
            return symbols;
        }
    }

    class JsonCurrency {
        /**
         * used to parse a JSON file with currency rates with
         */
        TreeMap<String, Double> rates;
    }

    class JsonCryptoSymbols{
        /**
         * used to parse a JSON file with crypto symbols with
         */
        String asset_id;
        String name;
    }

    class JsonCrypto{
        /**
         * used to parse a JSON file with crypto rates with
         */
        String asset_id_base;
        JsonCryptoRates[] rates;
    }

    class JsonCryptoRates {
        /**
         * used to set the objects parsed with the JsonCrypto class
         */
        String time;
        String asset_id_quote;
        Double rate;
    }
}

