public class Time extends ConverterSuperClass {

    String[] timeArray = {"Micro sec.", "Hours", "Milli sec.", "Days",
            "Seconds", "Weeks", "Minutes",  "Years"};

    public Time() {
        super("Standard", false, true);
        run(timeArray, 4);
    }

    @Override
    public void Converter(){
        double amount = Double.parseDouble(sb.toString());
        switch(currentFocus){
            case "Micro sec.":
                baseVal = amount / 1_000_000;
                break;
            case "Milli sec.":
                baseVal = amount / 1_000;
                break;
            case "Seconds":
                baseVal = amount;
                break;
            case "Minutes":
                baseVal = amount * 60;
                break;
            case "Hours":
                baseVal = amount * 60 * 60;
                break;
            case "Days":
                baseVal = amount * 60 * 60 * 24;
                break;
            case "Weeks":
                baseVal = amount * 60 * 60 * 24 * 7;
                break;
            case "Years":
                baseVal = amount * 60 * 60 * 24 * 365.25;
                break;
        }
        fromToCalculation();
        setFields();
    }

    @Override
    public void fromToCalculation(){
        amounts[0] = baseVal * 1_000_000;
        amounts[2] = baseVal * 1_000;
        amounts[4] = baseVal;
        amounts[6] = amounts[4] / 60;
        amounts[1] = amounts[6] / 60;
        amounts[3] = amounts[1] / 24;
        amounts[5] = amounts[3] / 7;
        amounts[7] = amounts[3] / 365.25;
    }
}
