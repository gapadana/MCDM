package policyPackage.resources;

public class ShortMaterial {
    private String name;
    private String code;
    private String unit;
    private String description;

    public ShortMaterial(String name, String code, String unit, String description) {
        this.name = name;
        this.code = code;
        this.unit = unit;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public ShortMaterial copy() {
        return new ShortMaterial(name, code, unit, description);
    }

}
