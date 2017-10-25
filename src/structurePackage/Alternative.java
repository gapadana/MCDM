package structurePackage;

import policyPackage.BillOfQuantities;
import policyPackage.Constants;
import policyPackage.Criterion;
import policyPackage.emissionFactors.EmissionFactorHandler;
import policyPackage.resources.Equipment;
import policyPackage.resources.Material;
import policyPackage.suppliers.Supplier;
import policyPackage.suppliers.SupplierHandler;

import java.util.LinkedHashMap;

public class Alternative {

    public LinkedHashMap<String, Criterion> criteria;

    private String alternativeName;
    public LinkedHashMap<String, Material> materials;
    public LinkedHashMap<String, Equipment> equipments;
    public LinkedHashMap<String, Double> resourcesCF = new LinkedHashMap<>();
    private LinkedHashMap<String, Double> deliveryCF = new LinkedHashMap<>();
    public LinkedHashMap<String, Double> equipmentCF = new LinkedHashMap<>();
//    private double cf2 = 0;
//    private double cf3 = 0;
    private double index;

    public Alternative(String alternativeName) {
        this.alternativeName = alternativeName;
        equipments = new LinkedHashMap<>();
        materials = new LinkedHashMap<>();
        criteria = new LinkedHashMap<>();
    }

    public void addCriteria(String criteriaName, Criterion criterion){
        criteria.put(criteriaName, criterion);
    }


    public String getAlternativeName() {
        return alternativeName;
    }

    public double getIndex() {
        return index;
    }

    public void setIndex(double index) {
        this.index = index;
    }

    public void CalculateCF() {

        double output = 0;
//        for (Material resource: materials) {
//            // TODO: 7/21/2017 use normalized model later
//            output += resource.getValue() * EmissionFactor2.i().getEF(resource.getName()).getValue();
//        }
//        criteria.put("CF", output);
    }

    public void addCriterion(String code, String name, double value) {
        criteria.put(code, new Criterion(code, name, value));
    }

    public void calculateCF() {

        double buildingCoEfficient = 0;
        if(alternativeName.toLowerCase().contains("roof") || alternativeName.toLowerCase().contains("structure")) {
            buildingCoEfficient = BillOfQuantities.i().getLandArea()
                    * BillOfQuantities.i().getOccupiedArea() / 100
                    * (BillOfQuantities.i().getStoriesAG() + BillOfQuantities.i().getStoriesUG());
        }else {
            buildingCoEfficient = BillOfQuantities.i().getLandPerimeter()
                    * BillOfQuantities.i().getStoriesUG() * Constants.FLOOR_HEIGHT
                    * BillOfQuantities.i().getOccupiedArea() / 100;
        }

        for (Material material : materials.values()) {
            double materialQuantity = material.getAvg() * buildingCoEfficient;
            resourcesCF.put(material.getCode(), materialQuantity
                    * EmissionFactorHandler.i().getEf(material.getCode()).getAvg());
            Supplier selectedSupplier = SupplierHandler.i().getSelectedSupplier(material.getCode());
            if(selectedSupplier != null) {
                deliveryCF.put(material.getCode(),
                        materialQuantity / selectedSupplier.getCapacity()
                                * selectedSupplier.getDistance() / Constants.AVERAGE_SPEED
                                * selectedSupplier.getAve());
            }else{
                deliveryCF.put(material.getCode(),
                        materialQuantity / 100
                                * selectedSupplier.getDistance() / Constants.AVERAGE_SPEED
                                * selectedSupplier.getAve());
            }
        }

        for (Equipment eqp : equipments.values()) {
            equipmentCF.put(eqp.getCode(), eqp.getAvg() * buildingCoEfficient
                    * EmissionFactorHandler.i().getEf(eqp.getCode()).getAvg());
        }

        double cf = 0;
        for (double cfTemp: resourcesCF.values()) {
            cf += cfTemp;
        }
        for (double cfTemp: deliveryCF.values()) {
            cf += cfTemp;
        }
        for (double cfTemp: equipmentCF.values()) {
            cf += cfTemp;
        }
        Criterion cfC = new Criterion("CF", "CF", cf);
        criteria.put("CF", cfC);
    }

    public double getResourceCF(){
        double result = 0;
        for (double each:resourcesCF.values()){
            result += each;
        }
        return result;
    }

    public double getDeliveryCF(){
        double result = 0;
        for (double each:deliveryCF.values()){
            result += each;
        }
        return result;
    }

    public double getEquipmentCF(){
        double result = 0;
        for (double each:equipmentCF.values()){
            result += each;
        }
        return result;
    }
}
