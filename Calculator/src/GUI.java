import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame{
    private JMenu menu;

    private Dimension panelDimensions = new Dimension(404,725);

    private JPanel cardPanel;

    private final static String CALCULATORPANEL = "Calculator";
    private final static String DATESPANEL = "Dates";
    private final static String CURRENCYPANEL = "Currency";
    private final static String CRYPTOPANEL = "Crypto";
    private final static String VOLUMEPANEL = "Volume";
    private final static String LENGTHPANEL = "Length";
    private final static String WEIGHTANDMASSPANEL = "WeightAndMass";
    private final static String TEMPERATUREPANEL = "Temperature";
    private final static String ENERGYPANEL = "Energy";
    private final static String POWERPANEL = "Power";
    private final static String SURFACEPANEL = "Surface";
    private final static String SPEEDPANEL = "Speed";
    private final static String PRESSUREPANEL = "Pressure";
    private final static String TIMEPANEL = "Time";
    private final static String ANGLEPANEL = "Angle";

    private final static String[] itemNames = {"Calculator","Dates","Currency","Crypto","Volume",
            "Length","WeightAndMass","Temperature","Energy","Power","Surface","Speed","Pressure",
            "Time","Angle"};


    private void cardInitialiser(){
        cardPanel = new JPanel(new CardLayout());
        cardPanel.add(new Calculator(), CALCULATORPANEL);
        cardPanel.add(new Dates2(), DATESPANEL);
        cardPanel.add(new Currency(), CURRENCYPANEL);
        cardPanel.add(new Crypto(), CRYPTOPANEL);
        cardPanel.add(new Volume(), VOLUMEPANEL);
        cardPanel.add(new Length(), LENGTHPANEL);
        cardPanel.add(new WeightAndMass(), WEIGHTANDMASSPANEL);
        cardPanel.add(new Temperature(), TEMPERATUREPANEL);
        cardPanel.add(new Energy(), ENERGYPANEL);
        cardPanel.add(new Power(), POWERPANEL);
        cardPanel.add(new Surface(), SURFACEPANEL);
        cardPanel.add(new Speed(), SPEEDPANEL);
        cardPanel.add(new Pressure(), PRESSUREPANEL);
        cardPanel.add(new Time(), TIMEPANEL);
        cardPanel.add(new Angle(), ANGLEPANEL);
    }

    private GUI(){}

    class MenuActionListener implements ActionListener{
        /**
         * An listens for the choice made in the menu bar and puts the correct card on top
         * @param e = the chosen option in the menu bar
         */
        @Override
        public void actionPerformed( ActionEvent e) {
            String command = e.getActionCommand();
            validate();
            repaint();
            pack();
            CardLayout c1 = (CardLayout) (cardPanel.getLayout());
            c1.show(cardPanel, command);
            menu.setText("Menu - " + command);
        }
    }

    private void createMenuBar(){
        /**
         * Creates the menu bar on top of the program
         */
        MenuActionListener mal = new MenuActionListener();
        menu = new JMenu("Menu");

        for(int i = 0; i < itemNames.length; i++){
            JMenuItem menuItem = new JMenuItem(itemNames[i]);
            menuItem.addActionListener(mal);
            menu.add(menuItem);
        }
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }

    private void createAndShowUI(){
        /** This method sets up the GUI */
        cardInitialiser();
        createMenuBar();
        cardPanel.setPreferredSize(panelDimensions);
        getContentPane().add(cardPanel, BorderLayout.NORTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
        setResizable(false);
        getContentPane().setBackground(ColorUIResource.getHSBColor(0.31f,0.09f,0.97f));
    }

    public static void main(String[] args)
        /**
         * Swing elements aren't thread safe, therefore we need to run the below function to make it safe for threads
         */
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new GUI().createAndShowUI();
            }
        });
    }
}
