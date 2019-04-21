public class Speed extends ConverterSuperClass{
    String[] speedArray = {"cM.p.Sec.", "Meter p.sec.", "KpH", "Foot p.sec.", "MpH", "Knots",
            "Mach"};

    public Speed() {
        super("Standard", false, true);
        run(speedArray, 1);
    }

    @Override
    public void Converter(){
        double amount = Double.parseDouble(sb.toString());
        switch(currentFocus){
            case "cM.p.Sec.":
                baseVal = amount / 100;
                break;
            case "Meter p.sec.":
                baseVal = amount;
                break;
            case "KpH":
                baseVal = amount * 0.277777777777778;
                break;
            case "Foot p.sec.":
                baseVal = amount * 0.3048;
                break;
            case "MpH":
                baseVal = amount * 0.447;
                break;
            case "Knots":
                baseVal = amount * 0.5144;
                break;
            case "Mach":
                baseVal = amount * 340.30;
                break;
            }
        fromToCalculation();
        setFields();
    }

    @Override
    public void fromToCalculation(){
        amounts[0] = baseVal * 100;
        amounts[1] = baseVal;
        amounts[2] = baseVal * 3.60000000000000;
        amounts[3] = baseVal * 3.28083989501312;
        amounts[4] = baseVal * 2.23713646532438;
        amounts[5] = baseVal * 1.94401244167963;
        amounts[6] = baseVal * 0.0029385836027035;
    }
}
