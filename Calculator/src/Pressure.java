public class Pressure extends ConverterSuperClass {
    String[] pressureArray = {"Atmosphere", "Bar", "Pascal",
            "Kilo pascal", "Torr", "P.P.Sqr.Inch"};

    public Pressure() {
        super("Standard", false, true);
        run(pressureArray, 3);
    }

    @Override
    public void Converter(){
        double amount = Double.parseDouble(sb.toString());
        switch(currentFocus){
            case "Atmosphere":
                baseVal = amount * 101.325;
                break;
            case "Bar":
                baseVal = amount * 100;
                break;
            case "Pascal":
                baseVal = amount * 0.001;
                break;
            case "Kilo pascal":
                baseVal = amount;
                break;
            case "Torr":
                baseVal = amount * 0.133299999999996;
                break;
            case "P.P.Sqr.Inch":
                baseVal = amount * 6.89475700000002;
                break;
        }
        fromToCalculation();
        setFields();
    }

    @Override
    public void fromToCalculation(){
        amounts[0] = baseVal * 0.0098692326671601;
        amounts[1] = baseVal * 0.0099999999999999;
        amounts[2] = baseVal * 1000;
        amounts[3] = baseVal;
        amounts[4] = baseVal * 7.50187546886745;
        amounts[5] = baseVal * 0.145037743897283;
    }
}
