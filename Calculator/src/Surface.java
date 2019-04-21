public class Surface extends ConverterSuperClass {
    String[] surfaceArray = {"Sqr.mM", "Sqr.cM", "Sqr.M","Hectare", "Sqr.KM",
            "Sqr.inch", "Sqr.foot", "Sqr.yard", "Acres", "Sqr.Mile"};

    public Surface() {
        super("Standard", false, true);
        run(surfaceArray, 2);
    }

    @Override
    public void Converter(){
        double amount = Double.parseDouble(sb.toString());
        switch(currentFocus){
            case "Sqr.mM":
                baseVal = amount / 1_000_000;
                break;
            case "Sqr.cM":
                baseVal = amount / 10_000;
                break;
            case "Sqr.M":
                baseVal = amount;
                break;
            case "Hectare":
                baseVal = amount * 10_000;
                break;
            case "Sqr.KM":
                baseVal = amount * 1_000_000;
                break;
            case "Sqr.inch":
                baseVal = amount * 0.00064516;
                break;
            case "Sqr.foot":
                baseVal = amount * 0.09290304;
                break;
            case "Sqr.yard":
                baseVal = amount * 0.83612736;
                break;
            case "Acres":
                baseVal = amount * 4_046.85642240;
                break;
            case "Sqr.Mile":
                baseVal = amount * 2_589_988.110336;
                break;
        }
        fromToCalculation();
        setFields();
    }

    @Override
    public void fromToCalculation(){
        amounts[0] = baseVal * 1_000_000;
        amounts[1] = baseVal * 10_000;
        amounts[2] = baseVal;
        amounts[3] = baseVal / 10_000;
        amounts[4] = baseVal / 1_000_000;
        amounts[5] = baseVal * 1_550.0031000062;
        amounts[6] = baseVal * 10.76391041671;
        amounts[7] = baseVal * 1.19599004630;
        amounts[8] = baseVal * 0.00024710538;
        amounts[9] = baseVal * 0.00000038610216;
    }
}
