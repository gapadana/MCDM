package policyPackage.emissionFactors;

import java.util.HashMap;

public class EmissionFactor {

    private String name;
    private double avg;
    private double min;
    private double max;
    private String unit;

    public EmissionFactor(String title) {
        if(title.contains("(")){
            if(title.contains("(") && title.contains(")")){
                String[] split = title.split("\\(");
                name = split[0].trim();
                unit = split[1].replaceAll("\\)","").trim();
            }
        }else{
            name = title;
        }
    }

    private EmissionFactor(String name, String unit) {
        this.name = name;
        this.unit = unit;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public String getName() {
        return name;
    }

    public double getAvg() {
        return avg;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public String getUnit() {
        return unit;
    }

    EmissionFactor newInstance() {
        EmissionFactor temp = new EmissionFactor(name, unit);
        temp.setAvg(getAvg());
        temp.setMax(getMax());
        temp.setMin(getMin());
        return temp;
    }
}
