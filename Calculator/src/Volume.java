import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Volume extends JPanel{
    private OptionActionListener oal;
    private JPanel cardPanel;

    private final static String STANDARDPANEL = "Standard volume";
    private final static String KITCHENPANEL = "Kitchen volume";

    public Volume() {
        /**
         * Custom Panel that contains two card panels, Standard volume and Kitchen Volume
         */
        oal = new OptionActionListener();
        this.setLayout(new MigLayout());
        this.setBackground(ColorUIResource.getHSBColor(0.31f,0.09f,0.97f));

        JComboBox type = new JComboBox();
        type.setBackground(Color.WHITE);
        type.addActionListener(oal);

        JPanel typePanel = new JPanel( new BorderLayout());
        typePanel.setOpaque(false);

        cardPanel = new JPanel(new CardLayout());
        JPanel card1 = new JPanel(new MigLayout());
        card1.add(new Standard(), "dock north");
        JPanel card2 = new JPanel(new MigLayout());
        card2.add(new Kitchen(), "dock north");
        type.addItem(STANDARDPANEL);
        type.addItem(KITCHENPANEL);
        cardPanel.add(card1, STANDARDPANEL);
        cardPanel.add(card2, KITCHENPANEL);

        typePanel.add(type, BorderLayout.WEST);

        this.add(typePanel, "dock north, wrap, width 180, height 20");
        this.add(cardPanel, "dock north, wrap, width 400, height 680");
    }

    public class Standard extends ConverterSuperClass{

        String[] standardVolumeArray = {"Milliliter", "Cubic cm.", "Liter",
                "Cubic meter", "Cubic inch", "Cubic foot", "Cubic yard"};

        public Standard() {
            super("Standard", false, true);
            run(standardVolumeArray, 1);
        }

        @Override
        public void Converter(){
            double amount = Double.parseDouble(sb.toString());
            switch(currentFocus){
                case "Milliliter":
                    baseVal = amount * 0.000001;
                    break;
                case "Cubic cm.":
                    baseVal = amount * 0.000001;
                    break;
                case "Liter":
                    baseVal = amount * 0.001;
                    break;
                case "Cubic meter":
                    baseVal = amount;
                    break;
                case "Cubic inch":
                    baseVal = amount * 0.000016387064;
                    break;
                case "Cubic foot":
                    baseVal = amount * 0.028316846592;
                    break;
                case "Cubic yard":
                    baseVal = amount * 0.764554857984;
                    break;
            }
            fromToCalculation();
            setFields();
        }

        @Override
        public void fromToCalculation(){
            amounts[0] = baseVal * 1_000_000;
            amounts[1] = baseVal * 1_000_000;
            amounts[2] = baseVal * 1_000;
            amounts[3] = baseVal;
            amounts[4] = baseVal * 61_023.74440947323;
            amounts[5] = baseVal * 35.3146667214886;
            amounts[6] = baseVal * 1.30795061931439;
        }
    }

    public class Kitchen extends  ConverterSuperClass{

        String[] kitchenVolumeArray = {"Milliliter", "Teaspoon", "Tablespoon","Fluid ounces",
                "Cups", "Pints", "Quarts", "Gallons"};

        public Kitchen() {
            super("Standard", false, true);
            run(kitchenVolumeArray, 1);
        }

        @Override
        public void Converter(){
            double amount = Double.parseDouble(sb.toString());
            switch(currentFocus){
                case "Milliliter":
                    baseVal = amount * 0.033814022701843;
                    break;
                case "Teaspoon":
                    baseVal = amount * 0.166666680403613;
                    break;
                case "Tablespoon":
                    baseVal = amount * 0.5;
                    break;
                case "Fluid ounces":
                    baseVal = amount * 0.0175842666666667;
                    break;
                case "Cups":
                    baseVal = amount * 8;
                    break;
                case "Pints":
                    baseVal = amount * 16;
                    break;
                case "Quarts":
                    baseVal = amount * 32;
                    break;
                case "Gallons":
                    baseVal = amount * 128;
                    break;

            }
            fromToCalculation();
            setFields();
        }

        @Override
        public void fromToCalculation(){
            amounts[0] = baseVal * 29.5735295625;
            amounts[1] = baseVal * 6;
            amounts[2] = baseVal * 2;
            amounts[3] = baseVal;
            amounts[4] = baseVal * 0.125;
            amounts[5] = baseVal * 0.0625;
            amounts[6] = baseVal * 0.03125;
            amounts[7] = baseVal * 0.007813;
        }
    }

    class OptionActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox jcb = (JComboBox) e.getSource();
            System.out.println(jcb.getSelectedItem().toString());
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, jcb.getSelectedItem().toString());
        }
    }
}
