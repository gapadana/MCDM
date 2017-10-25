package policyPackage.resources;

public class ShortEquipment {
    private String name;
    private String code;
    private String unit;
    private String energySource;
    private double power;

    public ShortEquipment(String name, String code, String unit, String energySource, double power) {
        this.name = name;
        this.code = code;
        this.unit = unit;
        this.energySource = energySource;
        this.power = power;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getUnit() {
        return unit;
    }

    public String getEnergySource() {
        return energySource;
    }

    public double getPower() {
        return power;
    }

    public ShortEquipment copy(){
        return new ShortEquipment(name, code, unit, energySource, power);
    }

}
