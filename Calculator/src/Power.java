public class Power extends ConverterSuperClass{

    String[] powerArray = {"Watt", "Kilo Watt", "Horse power",
            "Foot P.P.M.", "BTUs P.M."};

    public Power() {
        super("Standard", false, true);
        run(powerArray, 1);
    }

    @Override
    public void Converter(){
        double amount = Double.parseDouble(sb.toString());
        switch(currentFocus){
            case "Watt":
                baseVal = amount * 0.001;
                break;
            case "Kilo Watt":
                baseVal = amount;
                break;
            case "Horse power":
                baseVal = amount * 0.74569987158227;
                break;
            case "Foot P.P.M.":
                baseVal = amount * 0.0000225969658055233;
                break;
            case "BTUs P.M.":
                baseVal = amount * 0.0175842666666667;
                break;

        }
        fromToCalculation();
        setFields();
    }

    @Override
    public void fromToCalculation(){
        amounts[0] = baseVal * 1_000;
        amounts[1] = baseVal;
        amounts[2] = baseVal * 1.34102208959503;
        amounts[3] = baseVal * 44253.7289566360;
        amounts[4] = baseVal * 56.8690192748063;
    }
}
