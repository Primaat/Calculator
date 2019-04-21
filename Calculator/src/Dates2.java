import net.miginfocom.swing.MigLayout;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
//import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.lang.Math.abs;

public class Dates2 extends JPanel {
    /**
     * Panel for date calculation
     */
    private List<List<Integer>> monthLengthsList = new ArrayList<>();

    private DefaultComboBoxModel sModel28;
    private DefaultComboBoxModel sModel29;
    private DefaultComboBoxModel sModel30;
    private DefaultComboBoxModel sModel31;

    private DefaultComboBoxModel eModel28;
    private DefaultComboBoxModel eModel29;
    private DefaultComboBoxModel eModel30;
    private DefaultComboBoxModel eModel31;

    private DefaultComboBoxModel cModel28;
    private DefaultComboBoxModel cModel29;
    private DefaultComboBoxModel cModel30;
    private DefaultComboBoxModel cModel31;

    private List<String> years = new ArrayList<>();
    private String[] months = {"1","2","3","4","5","6","7","8","9","10","11","12"};
    private int[] monthDays = {31,28,31,30,31,30,31,31,30,31,30,31};

    private Integer[] thirtyOne = {1,3,5,7,8,10,12}; // months with 31 secondRight
    private Integer[] thirty = {4,6,9,11}; // months with 30 secondRight

    private Integer[] nineNineNine = new Integer[1000];
    private DefaultComboBoxModel model999y;
    private DefaultComboBoxModel model999m;
    private DefaultComboBoxModel model999d;

    private OptionActionListener oal;

    private JTextField diffResult;
    private JTextField diffResultInDays;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");

    private final static String DATEDIFFERENCESPANEL = "Difference between dates";
    private final static String DATECALCULATIONPANEL = "Add or subtract days";

    private JPanel cardPanel;


    public Dates2(){
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
        card1.add(new DateDifferences(), BorderLayout.WEST);
        card1.setBackground(ColorUIResource.getHSBColor(0.31f,0.09f,0.97f));
        JPanel card2 = new JPanel(new MigLayout());
        card2.add(new DateCalculation(), BorderLayout.WEST);
        card2.setBackground(ColorUIResource.getHSBColor(0.31f,0.09f,0.97f));
        type.addItem(DATEDIFFERENCESPANEL);
        type.addItem(DATECALCULATIONPANEL);
        cardPanel.add(card1, DATEDIFFERENCESPANEL);
        cardPanel.add(card2, DATECALCULATIONPANEL);

        typePanel.add(type, BorderLayout.WEST);

        this.add(typePanel, "gap left 10, dock north, wrap, width 180, height 20");
        this.add(cardPanel, "dock north, wrap, width 400, height 680");
    }

    class DateDifferences extends JPanel {
        /**
         * Custom innerclass for calculating the difference between two dates, selected by the user
         */
        private JComboBox startDayComboBox;
        private JComboBox startMonthComboBox;
        private JComboBox startYearComboBox;

        private JComboBox endDayComboBox;
        private JComboBox endMonthComboBox;
        private JComboBox endYearComboBox;

        private int startYear;
        private int startMonth;
        private int startDay;

        private int endYear;
        private int endMonth;
        private int endDay;

        private DayActionListener dal;
        private MonthActionListener mal;
        private YearActionListener yal;

        private EndDayActionListener edal;
        private EndMonthActionListener emal;
        private EndYearActionListener eyal;

        private Date startDate;
        private Date endDate;

        private DateDifferences(){

            dal = new DayActionListener();
            mal = new MonthActionListener();
            yal = new YearActionListener();

            edal = new EndDayActionListener();
            emal = new EndMonthActionListener();
            eyal = new EndYearActionListener();

            this.setLayout(new MigLayout());
            this.setBackground(ColorUIResource.getHSBColor(0.31f,0.09f,0.97f));

            setStartingDates();
            ArrayFiller();
            setStartUpComboBoxes();

            startDayComboBox.addActionListener(dal);
            startMonthComboBox.addActionListener(mal);
            startYearComboBox.addActionListener(yal);
            endDayComboBox.addActionListener(edal);
            endMonthComboBox.addActionListener(emal);
            endYearComboBox.addActionListener(eyal);

            JTextField startDateText = new JTextField("Start Date");
            startDateText.setOpaque(false);
            startDateText.setBorder(BorderFactory.createEmptyBorder());
            startDateText.setEditable(false);
            startDateText.setText("Start date");

            JPanel startDatePanel = new JPanel(new MigLayout());
            startDatePanel.setBorder(BorderFactory.createEmptyBorder());
            startDatePanel.setOpaque(false);
            startDatePanel.add(startDayComboBox, "gap left 10, dock west, height 20, width 50");
            startDatePanel.add(startMonthComboBox, "dock west, height 20, width 50");
            startDatePanel.add(startYearComboBox, "dock west, height 20, width 50");

            JTextField endDateText = new JTextField("End Date");
            endDateText.setOpaque(false);
            endDateText.setBorder(BorderFactory.createEmptyBorder());
            endDateText.setEditable(false);
            endDateText.setText("End date");

            JPanel endDatePanel = new JPanel(new MigLayout());
            endDatePanel.setBorder(BorderFactory.createEmptyBorder());
            endDatePanel.setOpaque(false);
            endDatePanel.add(endDayComboBox, "gap left 10, dock west, height 20, width 50");
            endDatePanel.add(endMonthComboBox, "dock west, height 20, width 50");
            endDatePanel.add(endYearComboBox, "dock west, height 20, width 50");

            JTextField differenceText = new JTextField("Difference");
            differenceText.setBorder(BorderFactory.createEmptyBorder());
            differenceText.setOpaque(false);
            differenceText.setAlignmentY(BOTTOM_ALIGNMENT);
            differenceText.setEditable(false);
            differenceText.setText("Difference");

            diffResult = new JTextField();
            diffResult.setOpaque(false);
            diffResult.setBorder(BorderFactory.createEmptyBorder());
            diffResult.setFont(new Font("Arial BLACK", Font.BOLD, 18));
            diffResult.setEditable(false);
            diffResult.setText("Dates are the same!");

            diffResultInDays = new JTextField();
            diffResultInDays.setOpaque(false);
            diffResultInDays.setBorder(BorderFactory.createEmptyBorder());
            diffResultInDays.setEditable(false);

            this.add(startDateText,"gap left 10, dock north, wrap, width 400,height 30");
            this.add(startDatePanel, "gap left 10, dock north, wrap, width 400, height 20");
            this.add(endDateText,"gap left 10, dock north, wrap, width 400,height 30");
            this.add(endDatePanel, "gap left 10, dock north, wrap, width 400, height 20");
            this.add(differenceText, "gap left 10, dock north, wrap, width 400,height 30");
            this.add(diffResult, "gap left 10, gap left 10, dock north, wrap, width 500,height 30");
            this.add(diffResultInDays, "gap left 10, dock north, wrap, width 400,height 30");
        }

        private void setStartingDates(){
            Date date = new Date();
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            startYear = localDate.getYear();
            startMonth = localDate.getMonthValue();
            startDay = localDate.getDayOfMonth();

            endYear = localDate.getYear();
            endMonth = localDate.getMonthValue();
            endDay = localDate.getDayOfMonth();
        }

        private void setStartUpComboBoxes(){
            if(startYear % 4 == 0 && startYear % 400 == 0 && startMonth == 2){
                startDayComboBox = new JComboBox(sModel29);
                endDayComboBox = new JComboBox(eModel29);
            }
            else if(Arrays.asList(thirty).contains(startMonth)){
                startDayComboBox = new JComboBox(sModel30);
                endDayComboBox = new JComboBox(eModel30);

            }
            else if(Arrays.asList(thirtyOne).contains(startMonth)){
                startDayComboBox = new JComboBox(sModel31);
                endDayComboBox = new JComboBox(eModel31);

            }
            else{
                startDayComboBox = new JComboBox(sModel28);
                endDayComboBox = new JComboBox(eModel28);
            }

            startDayComboBox.setMaximumRowCount(9);
            startDayComboBox.setBorder(BorderFactory.createEmptyBorder());
            startDayComboBox.setBackground(Color.WHITE);
            startDayComboBox.requestFocus();
            startDayComboBox.setSelectedIndex(startDay-1);
            endDayComboBox.setMaximumRowCount(9);
            endDayComboBox.setBorder(BorderFactory.createEmptyBorder());
            endDayComboBox.setBackground(Color.WHITE);
            endDayComboBox.requestFocus();
            endDayComboBox.setSelectedIndex(endDay-1);

            startMonthComboBox = new JComboBox(months);
            startMonthComboBox.setMaximumRowCount(9);
            startMonthComboBox.setBorder(BorderFactory.createEmptyBorder());
            startMonthComboBox.setBackground(Color.WHITE);
            startMonthComboBox.requestFocus();
            startMonthComboBox.setSelectedIndex(startMonth-1);
            endMonthComboBox = new JComboBox(months);
            endMonthComboBox.setMaximumRowCount(9);
            endMonthComboBox.setBorder(BorderFactory.createEmptyBorder());
            endMonthComboBox.setBackground(Color.WHITE);
            endMonthComboBox.requestFocus();
            endMonthComboBox.setSelectedIndex(endMonth-1);

            startYearComboBox = new JComboBox(years.toArray());
            startYearComboBox.setMaximumRowCount(9);
            startYearComboBox.setBorder(BorderFactory.createEmptyBorder());
            startYearComboBox.setBackground(Color.WHITE);
            startYearComboBox.requestFocus();
            startYearComboBox.setSelectedIndex(startYear-1600);
            endYearComboBox = new JComboBox(years.toArray());
            endYearComboBox.setMaximumRowCount(9);
            endYearComboBox.setBorder(BorderFactory.createEmptyBorder());
            endYearComboBox.setBackground(Color.WHITE);
            endYearComboBox.requestFocus();
            endYearComboBox.setSelectedIndex(endYear-1600);
        }

        private void dayComboBoxModelChanger(String startOrEnd){
            if(startOrEnd.equals("start")){
                if(startYear % 4 == 0 && startMonth == 2){
                    if(startYear % 400 == 0){
                        startDayComboBox.setModel(sModel29);
                    }
                    else if(startYear % 100 != 0){
                        startDayComboBox.setModel(sModel29);
                    }
                }
                else if(Arrays.asList(thirty).contains(startMonth)){
                    startDayComboBox.setModel(sModel30);
                }
                else if(Arrays.asList(thirtyOne).contains(startMonth)){
                    startDayComboBox.setModel(sModel31);
                }
                else{
                    startDayComboBox.setModel(sModel28);
                }

                if(startDay > startDayComboBox.getModel().getSize()){
                    startDayComboBox.setSelectedIndex(startDayComboBox.getModel().getSize()-1);
                }
                else{
                    startDayComboBox.setSelectedIndex(startDay-1);
                }
            }
            else if(startOrEnd.equals("end")){
                if(endYear % 4 == 0 && endMonth == 2){
                    if(endYear % 400 == 0){
                        endDayComboBox.setModel(eModel29);
                    }
                    else if(endYear % 100 != 0){
                        endDayComboBox.setModel(eModel29);
                    }
                }
                else if(Arrays.asList(thirty).contains(endMonth)){
                    endDayComboBox.setModel(eModel30);
                }
                else if(Arrays.asList(thirtyOne).contains(endMonth)){
                    endDayComboBox.setModel(eModel31);
                }
                else{
                    endDayComboBox.setModel(eModel28);
                }
                if(endDay > endDayComboBox.getModel().getSize()){
                    endDayComboBox.setSelectedIndex(endDayComboBox.getModel().getSize()-1);
                }
                else{
                    endDayComboBox.setSelectedIndex(endDay-1);
                }
            }
            compareDates();
        }

        private void compareDates(){
            int resultYears;
            int resultMonths;
            int weeks;
            int resultDays;
            Long dateDifference = 0L;
            StringBuilder sb = new StringBuilder();

            String rawStartDate = startDay + " " + startMonth + " " + startYear ;
            String rawEndDate = endDay + " " + endMonth + " " + endYear;
            try{
                startDate = dateFormat.parse(rawStartDate);
                endDate = dateFormat.parse(rawEndDate);
                dateDifference = startDate.getTime() - endDate.getTime();

            }catch (Exception e){
                e.printStackTrace();
            }

            if( dateDifference == 0){
                diffResult.setText("Dates are the same");
            }
            else{
                if(startYear > endYear){
                    resultYears = startYear - endYear;
                    resultMonths = startMonth - endMonth;
                    resultDays = startDay - endDay;
                }
                else{
                    resultYears = endYear - startYear;
                    resultMonths = endMonth - startMonth;
                    resultDays = endDay - startDay;
                }
                if(resultMonths < 0 && resultYears > 0){
                    resultYears -= 1;
                    resultMonths += 12;
                }
                if(resultDays < 0 && resultMonths > 0){
                    resultMonths -= 1;
                    if(endMonth == 2 && startMonth > endMonth || startMonth == 2 && endMonth > startMonth){
                        resultDays += 28;
                    }
                    else{
                        resultDays += 30;
                    }
                }
                else if(resultDays < 0 && resultMonths == 0 && resultYears > 0){
                    resultYears -= 1;
                    resultMonths += 11;
                    if(endMonth == 2 && startMonth > endMonth || startMonth == 2 && endMonth > startMonth){
                        resultDays += 28;
                    }
                    else{
                        resultDays += 30;
                    }
                }
                if(abs(resultYears) > 1){
                    sb.append(abs(resultYears) + " years; ");
                }
                else if(abs(resultYears) == 1){
                    sb.append(abs(resultYears) + " year; ");
                }
                if(abs(resultMonths) > 1){
                    sb.append(abs(resultMonths) + " months; ");
                }
                else if(abs(resultMonths) == 1){
                    sb.append(abs(resultMonths) + " month; ");
                }
                if(abs(resultDays) > 0){
                    int remainingDays = resultDays % 7;
                    weeks = (resultDays - remainingDays) / 7;
                    if(abs(weeks) > 1){
                        sb.append(abs(weeks) + " weeks; ");
                    }
                    else if(abs(weeks) == 1 ){
                        sb.append(abs(weeks) + " week; ");
                    }
                    if(abs(remainingDays) > 1){
                        sb.append(abs(remainingDays) + " days");
                    }
                    else if(abs(remainingDays) == 1 ){
                        sb.append(abs(remainingDays) + " day");
                    }
                }
                diffResult.setText(sb.toString());
                int leapDays = 0;
                if(startYear < endYear){
                    for(int start = startYear; start < endYear; start++ ){
                        if(start % 4 == 0) {
                            if ( start % 400 == 0 ) {
                                leapDays += 1;
                            } else if ( start % 100 != 0 ) {
                                leapDays += 1;
                            }
                        }
                    }
                }
                else{
                    for(int end = endYear; end < startYear; end++ ){
                        if(end % 4 == 0 && ((endMonth > 2) || (endMonth == 2 && endDay == 29))) {
                            if ( end % 400 == 0 ) {
                                leapDays += 1;
                            } else if ( end % 100 != 0 ) {
                                leapDays += 1;
                            }
                        }
                    }
                }
                int startYearDays = 0;
                for(int d = startMonth-1; d < 12; d++){
                    if(d == startMonth-1){
                        startYearDays += monthDays[d] - startDay;
                    }
                    else{
                        startYearDays += monthDays[d];
                    }
                }
                int endYearDays = 0;
                for(int e = 0; e < endMonth; e++){
                    if(e == endMonth-1){
                        endYearDays += endDay;
                    }
                    else{
                        endYearDays += monthDays[e];
                    }
                }
                int amountDays;
                int roundingDays = 0;
                if((endYear < startYear && endMonth < startMonth) || (startYear < endYear && startMonth < endMonth)){
                    if(endMonth < startMonth){
                        roundingDays = (startMonth - endMonth) * 7 / 12;
                    }
                    else {
                        roundingDays = (endMonth - startMonth) * 7 / 12;
                    }
                }
                amountDays = (resultYears * 365) + (resultMonths * 30) + resultDays + leapDays + roundingDays;
                diffResultInDays.setText(abs(amountDays) - 1 + " days");
            }
        }

        class DayActionListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox scb = (JComboBox)e.getSource();
                startDay = Integer.parseInt(scb.getSelectedItem().toString());
                startDayComboBox.requestFocus();
                startDayComboBox.setSelectedIndex(startDay-1);
                compareDates();
            }
        }
        class MonthActionListener implements  ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox scb = (JComboBox)e.getSource();
                startMonth = Integer.parseInt(scb.getSelectedItem().toString());
                startMonthComboBox.requestFocus();
                startMonthComboBox.setSelectedIndex(startMonth-1);
                dayComboBoxModelChanger("start");
            }
        }
        class YearActionListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox scb = (JComboBox)e.getSource();
                startYear = Integer.parseInt(scb.getSelectedItem().toString());
                startDayComboBox.requestFocus();
                startYearComboBox.requestFocus();
                startYearComboBox.setSelectedIndex(startYear-1600);
                dayComboBoxModelChanger("start");
            }
        }

        class EndDayActionListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                endDay = Integer.parseInt(cb.getSelectedItem().toString());
                endDayComboBox.requestFocus();
                endDayComboBox.setSelectedIndex(endDay-1);
                compareDates();
            }
        }
        class EndMonthActionListener implements  ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                endMonth = Integer.parseInt(cb.getSelectedItem().toString());
                endMonthComboBox.requestFocus();
                endMonthComboBox.setSelectedIndex(endMonth-1);
                dayComboBoxModelChanger("end");
            }
        }
        class EndYearActionListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                endYear = Integer.parseInt(cb.getSelectedItem().toString());
                endDayComboBox.requestFocus();
                endYearComboBox.requestFocus();
                endYearComboBox.setSelectedIndex(endYear-1600);
                dayComboBoxModelChanger("end");
            }
        }
    }
    class DateCalculation extends JPanel{
        /**
         * Custom innerclass for calculating a date
         */
        private JComboBox calcDayComboBox;
        private JComboBox calcMonthComboBox;
        private JComboBox calcYearComboBox;

        private JComboBox yearsBox;
        private JComboBox monthsBox;
        private JComboBox daysBox;

        private int calcStartDay;
        private int calcStartMonth;
        private int calcStartYear;

        private int yearAmount;
        private int monthsAmount;
        private int daysAmount;

        private CalcDayActionListener cdal;
        private CalcMonthActionListener cmal;
        private CalcYearActionListener cyal;

        private JTextField startDateText;
        private JPanel startDatePanel;
        private JRadioButton add;
        private JRadioButton subtract;
        private ButtonGroup group;
        private JPanel radioButtonPanel;
        private JTextField yearsText;
        private JTextField monthsText;
        private JTextField daysText;
        private JPanel yMdTexts;
        private JPanel yMdBoxes;
        private JTextField dateText;
        private JTextField dateField;

        private boolean areWeAdding = true;

        private addSubtractActionListener asal;

        private DateCalculation(){
            cdal = new CalcDayActionListener();
            cmal = new CalcMonthActionListener();
            cyal = new CalcYearActionListener();

            asal = new addSubtractActionListener();

            this.setLayout(new MigLayout());
            this.setBackground(ColorUIResource.getHSBColor(0.31f,0.09f,0.97f));

            setStartingDates();
            setStartUpComboBoxes();

            calcDayComboBox.addActionListener(cdal);
            calcMonthComboBox.addActionListener(cmal);
            calcYearComboBox.addActionListener(cyal);

            startDateText = new JTextField("Start Date");
            startDateText.setBackground(ColorUIResource.getHSBColor(0.31f,0.09f,0.97f));
            startDateText.setBorder(BorderFactory.createEmptyBorder());
            startDateText.setEditable(false);
            startDateText.setText("Start date");

            startDatePanel = new JPanel(new MigLayout());
            startDatePanel.setBackground(ColorUIResource.getHSBColor(0.31f,0.09f,0.97f));
            startDatePanel.add(calcDayComboBox, "dock west, height 20, width 50");
            startDatePanel.add(calcMonthComboBox, "dock west, height 20, width 50");
            startDatePanel.add(calcYearComboBox, "dock west, height 20, width 50");

            add = new JRadioButton("Add");
            add.setOpaque(false);
            add.setActionCommand("Add");
            add.setSelected(true);

            subtract = new JRadioButton("Subtract");
            subtract.setOpaque(false);
            subtract.setActionCommand("Subtract");

            group = new ButtonGroup();
            group.add(add);
            group.add(subtract);

            radioButtonPanel = new JPanel(new MigLayout());
            radioButtonPanel.setOpaque(false);
            radioButtonPanel.add(add, "gap left 10, dock west");
            radioButtonPanel.add(subtract, "dock west");

            add.addActionListener(new RadioActionListener());
            subtract.addActionListener(new RadioActionListener());

            yearsText = new JTextField("years");
            yearsText.setOpaque(false);
            yearsText.setEditable(false);
            yearsText.setBorder(BorderFactory.createEmptyBorder());

            monthsText = new JTextField("months");
            monthsText.setOpaque(false);
            monthsText.setEditable(false);
            monthsText.setBorder(BorderFactory.createEmptyBorder());

            daysText = new JTextField("days");
            daysText.setOpaque(false);
            daysText.setEditable(false);
            daysText.setBorder(BorderFactory.createEmptyBorder());

            yearsBox = new JComboBox(model999y);
            yearsBox.setBackground(Color.WHITE);
            yearsBox.addActionListener(asal);
            yearsBox.setMaximumRowCount(9);
            yearsBox.setActionCommand("years");

            monthsBox = new JComboBox(model999m);
            monthsBox.setBackground(Color.WHITE);
            monthsBox.addActionListener(asal);
            monthsBox.setMaximumRowCount(9);
            monthsBox.setActionCommand("months");

            daysBox = new JComboBox(model999d);
            daysBox.setBackground(Color.WHITE);
            daysBox.addActionListener(asal);
            daysBox.setMaximumRowCount(9);
            daysBox.setActionCommand("days");

            yMdTexts = new JPanel(new MigLayout());
            yMdTexts.setOpaque(false);
            yMdTexts.add(yearsText, "dock west, gapx 15, height 20, width 100, align left");
            yMdTexts.add(monthsText, "dock west, gapx 15, height 20, width 100, align left");
            yMdTexts.add(daysText, "dock west, gapx 15, wrap, height 20, width 100, align left");

            yMdBoxes = new JPanel(new MigLayout());
            yMdBoxes.setOpaque(false);
            yMdBoxes.add(yearsBox,"dock west, gapx 15, height 20, width 100");
            yMdBoxes.add(monthsBox,"dock west, gapx 15, height 20, width 100");
            yMdBoxes.add(daysBox, "dock west, gapx 15, height 20, width 100");

            dateText = new JTextField("Date");
            dateText.setOpaque(false);
            dateText.setBorder(BorderFactory.createEmptyBorder());
            dateText.setEditable(false);
            dateText.setText("Date");

            dateField = new JTextField("Date");
            dateField.setOpaque(false);
            dateField.setBorder(BorderFactory.createEmptyBorder());
            dateField.setEditable(false);
            dateField.setFont(new Font("Arial Black", Font.BOLD, 20));
            dateField.setText("result date: ");

            this.add(startDateText,"gap left 10, dock north, wrap, width 400,height 40");
            this.add(startDatePanel, "gap left 10, dock north, wrap, width 400, height 50");
            this.add(radioButtonPanel, "gap left 10, dock north, wrap, width 400, height 50");
            this.add(yMdTexts, "gap left 10, dock north, wrap, width 400, height 20");
            this.add(yMdBoxes, "gap left 10, dock north, wrap, width 400, height 50");
            this.add(dateText, "gap left 10, dock north, wrap, width 400,height 30");
            this.add(dateField, "gap left 10, dock north, wrap, width 400,height 30");
        }

        class RadioActionListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                if(command.equals("Add")){
                    areWeAdding = true;
                }
                else if(command.equals("Subtract")){
                    areWeAdding = false;
                }
                addSubtractSwitcher();
            }
        }
        private void addSubtractSwitcher(){
            if(areWeAdding){
                addingDates();
            }
            else{
                subtractingDates();
            }
        }

        private void addingDates(){
            String date = calcStartDay + "-" + calcStartMonth + "-" + calcStartYear;
            org.joda.time.format.DateTimeFormatter dtf = DateTimeFormat.forPattern("dd-MM-YYYY");
            DateTime dateTimeStart = dtf.parseDateTime(date);
            System.out.println(dateTimeStart + " A");

            if(yearAmount != 0){
                dateTimeStart = dateTimeStart.plusYears(yearAmount);
            }
            if(monthsAmount > 0 ){
                dateTimeStart = dateTimeStart.plusMonths(monthsAmount);
            }
            if(daysAmount != 0){
                dateTimeStart = dateTimeStart.plusDays(daysAmount);
            }
            DateTime resultDate = dateTimeStart;
            DateTimeFormatter dtfOut = DateTimeFormat.forPattern("EEEEEEEEE dd MMMMMMMMM yyyy");

            dateField.setText(dtfOut.print(resultDate));
        }

        private void subtractingDates(){
            String date = calcStartDay + "-" + calcStartMonth + "-" + calcStartYear;
            DateTimeFormatter dtf = DateTimeFormat.forPattern("dd-MM-YYYY");
            DateTime dateTimeStart = dtf.parseDateTime(date);
            if(yearAmount != 0){
                dateTimeStart = dateTimeStart.minusYears(yearAmount);
            }
            if(monthsAmount != 0){
                dateTimeStart = dateTimeStart.minusMonths(monthsAmount);
            }
            if(daysAmount != 0){
                dateTimeStart = dateTimeStart.minusDays(daysAmount);
            }
            DateTime resultDate = dateTimeStart;
            DateTimeFormatter dtfOut = DateTimeFormat.forPattern("EEEEEEEEE dd MMMMMMMMM yyyy");

            dateField.setText(dtfOut.print(resultDate));
        }

        private void setStartingDates(){
            Date date = new Date();
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            calcStartYear = localDate.getYear();
            calcStartMonth = localDate.getMonthValue();
            calcStartDay = localDate.getDayOfMonth();
        }

        private void setStartUpComboBoxes(){

            System.out.println(calcStartDay +"-"+ calcStartMonth +"-"+calcStartYear);
            if(calcStartYear % 4 == 0 && calcStartYear % 400 == 0 && calcStartMonth == 2){
                calcDayComboBox = new JComboBox(cModel29);
            }
            else if(Arrays.asList(thirty).contains(calcStartMonth)){
                calcDayComboBox = new JComboBox(cModel30);
            }
            else if(Arrays.asList(thirtyOne).contains(calcStartMonth)){
                calcDayComboBox = new JComboBox(cModel31);
            }
            else{
                calcDayComboBox = new JComboBox(cModel28);
            }

            calcDayComboBox.setMaximumRowCount(8);
            calcDayComboBox.setBackground(Color.WHITE);
            calcDayComboBox.requestFocus();
            calcDayComboBox.setSelectedIndex(calcStartDay-1);

            calcMonthComboBox = new JComboBox(months);
            calcMonthComboBox.setMaximumRowCount(8);
            calcMonthComboBox.setBackground(Color.WHITE);
            calcMonthComboBox.requestFocus();
            calcMonthComboBox.setSelectedIndex(calcStartMonth-1);

            calcYearComboBox = new JComboBox(years.toArray());
            calcYearComboBox.setMaximumRowCount(20);
            calcYearComboBox.setBackground(Color.WHITE);
            calcYearComboBox.requestFocus();
            calcYearComboBox.setSelectedIndex(calcStartYear-1600);
        }

        private void dayComboBoxModelChanger(){
            if(calcStartYear % 4 == 0 && calcStartMonth == 2){
                if(calcStartYear % 400 == 0){
                    calcDayComboBox.setModel(cModel29);
                }
                else if(calcStartYear % 100 != 0){
                    calcDayComboBox.setModel(cModel29);
                }
            }
            else if(Arrays.asList(thirty).contains(calcStartMonth)){
                calcDayComboBox.setModel(cModel30);
            }
            else if(Arrays.asList(thirtyOne).contains(calcStartMonth)){
                calcDayComboBox.setModel(cModel31);
            }
            else{
                calcDayComboBox.setModel(cModel28);
            }
            if(calcStartDay > calcDayComboBox.getModel().getSize()){
                calcDayComboBox.setSelectedIndex(calcDayComboBox.getModel().getSize()-1);
            }
            else{
                calcDayComboBox.setSelectedIndex(calcStartDay-1);
            }
        }

        class CalcDayActionListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox scb = (JComboBox)e.getSource();
                calcStartDay = Integer.parseInt(scb.getSelectedItem().toString());
                calcDayComboBox.requestFocus();
                calcDayComboBox.setSelectedIndex(calcStartDay-1);
                addSubtractSwitcher();
            }
        }
        class CalcMonthActionListener implements  ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox scb = (JComboBox)e.getSource();
                calcStartMonth = Integer.parseInt(scb.getSelectedItem().toString());
                calcMonthComboBox.requestFocus();
                calcMonthComboBox.setSelectedIndex(calcStartMonth-1);
                dayComboBoxModelChanger();
                addSubtractSwitcher();
            }
        }
        class CalcYearActionListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox scb = (JComboBox)e.getSource();
                calcStartYear = Integer.parseInt(scb.getSelectedItem().toString());
                calcDayComboBox.requestFocus();
                calcYearComboBox.requestFocus();
                calcYearComboBox.setSelectedIndex(calcStartYear-1600);
                dayComboBoxModelChanger();
                addSubtractSwitcher();
            }
        }

        class addSubtractActionListener implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                switch(command){
                    case "years":
                        yearAmount = yearsBox.getSelectedIndex();
                        break;
                    case "months":
                        monthsAmount = monthsBox.getSelectedIndex();
                        break;
                    case "days":
                        daysAmount = daysBox.getSelectedIndex();
                        break;
                    default:
                        break;
                }
                addSubtractSwitcher();
            }
        }
    }

    public void ArrayFiller(){
        /**
         * loop creates 4 monthLengths of respectably 28,29,30, and 31 secondRight and adds them to an array
         * adds secondRight to each created monthLength
         */
        for(int p = 0; p < 4; p++){
            monthLengthsList.add(new ArrayList<>());
        }
        for (int k = 1; k <= 31; k++) {
            if(k < 29){
                monthLengthsList.get(0).add(k);
            }
            if(k < 30){
                monthLengthsList.get(1).add(k);
            }
            if(k < 31){
                monthLengthsList.get(2).add(k);
            }
            if(k < 32){
                monthLengthsList.get(3).add(k);
            }
        }
        sModel28 = new DefaultComboBoxModel(monthLengthsList.get(0).toArray());
        sModel29 = new DefaultComboBoxModel(monthLengthsList.get(1).toArray());
        sModel30 = new DefaultComboBoxModel(monthLengthsList.get(2).toArray());
        sModel31 = new DefaultComboBoxModel(monthLengthsList.get(3).toArray());

        eModel28 = new DefaultComboBoxModel(monthLengthsList.get(0).toArray());
        eModel29 = new DefaultComboBoxModel(monthLengthsList.get(1).toArray());
        eModel30 = new DefaultComboBoxModel(monthLengthsList.get(2).toArray());
        eModel31 = new DefaultComboBoxModel(monthLengthsList.get(3).toArray());

        cModel28 = new DefaultComboBoxModel(monthLengthsList.get(0).toArray());
        cModel29 = new DefaultComboBoxModel(monthLengthsList.get(1).toArray());
        cModel30 = new DefaultComboBoxModel(monthLengthsList.get(2).toArray());
        cModel31 = new DefaultComboBoxModel(monthLengthsList.get(3).toArray());

        for(int x = 0; x < 1000; x++){
            nineNineNine[x] = x;
        }

        model999y = new DefaultComboBoxModel(nineNineNine);
        model999m = new DefaultComboBoxModel(nineNineNine);
        model999d = new DefaultComboBoxModel(nineNineNine);

        // adds fourthRight to the fourthRight List
        for(int y = 1600; y < 2500; y++){
            years.add(y + "");
        }
    }

    class OptionActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox jcb = (JComboBox) e.getSource();
            CardLayout cl = (CardLayout) cardPanel.getLayout();
            cl.show(cardPanel, jcb.getSelectedItem().toString());
        }
    }
}
