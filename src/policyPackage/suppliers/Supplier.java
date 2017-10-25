package policyPackage.suppliers;


public class Supplier {

    private String materialCode;
    private String site;
    private String unit;
    private String name;
    private String supplier;
    private double distance;
    private double capacity;
    private double power;
    private double ave;
    private double min;
    private double max;
    private String EFUnit;


    public Supplier(String materialCode) {
        this.materialCode = materialCode;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public void setAve(double ave) {
        this.ave = ave;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void setEFUnit(String EFUnit) {
        this.EFUnit = EFUnit;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public String getSite() {
        return site;
    }

    public String getUnit() {
        return unit;
    }

    public String getName() {
        return name;
    }

    public String getSupplier() {
        return supplier;
    }

    public double getDistance() {
        return distance;
    }

    public double getCapacity() {
        return capacity;
    }

    public double getPower() {
        return power;
    }

    public double getAve() {
        return ave;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }
}
