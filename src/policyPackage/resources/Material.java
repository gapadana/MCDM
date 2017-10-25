package policyPackage.resources;

import policyPackage.suppliers.Supplier;

public class Material extends Resource {

    private String description;
    private double ef;
    private double value;
    private Supplier supplier;

    public Material(String name, String description, String code, String unit) {
        setName(name);
        setCode(code);
        setUnit(unit);
        this.description = description;
    }


    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getEf() {
        return ef;
    }

    public void setEf(double ef) {
        this.ef = ef;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
