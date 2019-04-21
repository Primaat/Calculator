public class Length extends ConverterSuperClass {

    String[] lengthArray = {"Nanometer", "Millimeter", "Centimeter","Meter", "Kilometer",
            "Inch", "Foot", "Yard", "Mile", "Sea Mile"};

    public Length() {
        super("Standard", false, true);
        run(lengthArray, 2);
    }

    @Override
    public void Converter(){
        double amount = Double.parseDouble(sb.toString());
        switch(currentFocus){
            case "Nanometer":
                baseVal = amount / 10_000_000;
                break;
            case "Millimeter":
                baseVal = amount / 10;
                break;
            case "Centimeter":
                baseVal = amount;
                break;
            case "Meter":
                baseVal = amount * 100;
                break;
            case "Kilometer":
                baseVal = amount * 100_000;
                break;
            case "Inch":
                baseVal = amount * 2.54;
                break;
            case "Foot":
                baseVal = amount * 30.48;
                break;
            case "Yard":
                baseVal = amount * 91.44;
                break;
            case "Mile":
                baseVal = amount * 160_934.4;
                break;
            case "Sea Mile":
                baseVal = amount * 185_200;
                break;
        }
        fromToCalculation();
        setFields();
    }

    @Override
    public void fromToCalculation(){
        amounts[0] = baseVal * 10_000_000;
        amounts[1] = baseVal * 10;
        amounts[2] = baseVal;
        amounts[3] = baseVal / 100;
        amounts[4] = baseVal / 100_000;
        amounts[5] = baseVal * 0.3937008;
        amounts[6] = baseVal * 0.0328084;
        amounts[7] = baseVal * 0.01093613;
        amounts[8] = baseVal * 0.000006213712;
        amounts[9] = baseVal * 0.000005399568;
    }
}
