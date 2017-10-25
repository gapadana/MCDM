package policyPackage.resources;

public class Equipment extends Resource {

    private String energySource;
    private double power;

    public Equipment(String name, String code, String energySource, Double power, String unit) {
        setName(name);
        setCode(code);
        setUnit(unit);
        this.power = power;
        this.energySource = energySource;
    }

    public String getEnergySource() {
        return energySource;
    }

    public double getPower() {
        return power;
    }

}
