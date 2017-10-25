package policyPackage;

/**
 * Created by aebra on 7/28/2017.
 */
public class Emission {
    private String name;
    private String unit;
    private double value;

    public Emission(String name, String unit, double value) {
        this.name = name;
        this.unit = unit;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public double getValue() {
        return value;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeValue(double value) {
        this.value = value;
    }

    public void changeUnit(String unit) {
        this.unit = unit;
    }
}
