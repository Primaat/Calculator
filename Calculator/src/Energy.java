public class Energy extends ConverterSuperClass{

    String[] energyArray = {"Joules", "Kilojoules", "Thermal cal.",
            "Kilo cal.", "F.P.", "BTU"};

    public Energy() {
        super("Standard", false, true);
        run(energyArray, 1);
    }

    @Override
    public void Converter(){
        double amount = Double.parseDouble(sb.toString());
        switch(currentFocus){
            case "Joules":
                baseVal = amount * 0.001;
                break;
            case "Kilojoules":
                baseVal = amount;
                break;
            case "Thermal cal.":
                baseVal = amount * 0.004184;
                break;
            case "Kilo cal.":
                baseVal = amount * 4.184;
                break;
            case "F.P.":
                baseVal = amount * 0.0013558179483314;
                break;
            case "BTU":
                baseVal = amount * 1.055056;
                break;
        }
        fromToCalculation();
        setFields();
    }

    @Override
    public void fromToCalculation(){
        amounts[0] = baseVal * 1_000;
        amounts[1] = baseVal;
        amounts[2] = baseVal * 239.005736137667;
        amounts[3] = baseVal * 0.239005736137667;
        amounts[4] = baseVal * 737.562149277266;
        amounts[5] = baseVal * 0.947816987913438;
    }
}
