public class WeightAndMass extends ConverterSuperClass {
    String[] weightArray = {"Carats", "ml.grams", "Grams","Kilograms", "Metric tons",
            "Ounces", "Pounds", "Stone", "Short tons", "Long tons"};

    public WeightAndMass() {
        super("Standard", false, true);
        run(weightArray, 2);
    }

    @Override
    public void Converter(){
        double amount = Double.parseDouble(sb.toString());
        switch(currentFocus){
            case "Carats":
                baseVal = amount * 0.2;
                break;
            case "ml.grams":
                baseVal = amount * 0.001;
                break;
            case "Grams":
                baseVal = amount;
                break;
            case "Kilograms":
                baseVal = amount * 1_000;
                break;
            case "Metric tons":
                baseVal = amount * 1_000_000;
                break;
            case "Ounces":
                baseVal = amount * 28.34952;
                break;
            case "Pounds":
                baseVal = amount * 453.59237;
                break;
            case "Stone":
                baseVal = amount * 6_350.293;
                break;
            case "Short tons":
                baseVal = amount * 907_184.7;
                break;
            case "Long tons":
                baseVal = amount * 1_016_047;
                break;
        }
        fromToCalculation();
        setFields();
    }

    @Override
    public void fromToCalculation(){
        amounts[0] = baseVal * 5;
        amounts[1] = baseVal * 1_000;
        amounts[2] = baseVal;
        amounts[3] = baseVal / 1_000;
        amounts[4] = baseVal / 1_000_000;
        amounts[5] = baseVal * 0.0352739619495804;
        amounts[6] = baseVal * 0.00220462262184878;
        amounts[7] = baseVal * 0.00015747304441777;
        amounts[8] = baseVal * 0.00000110231131092439;
        amounts[9] = baseVal * 0.000000984206527611061;
    }
}
