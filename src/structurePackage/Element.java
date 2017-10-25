package structurePackage;

import policyPackage.Criterion;
import policyPackage.Weights;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.StrictMath.abs;

/**
 * Created by aebra on 7/21/2017.
 */
public class Element {

    public String elementName;
    public HashMap<String, Alternative> alternatives;
    private HashMap<String, Double> maxCriteria;

    public Element(String elementName) {
        this.elementName = elementName;
        alternatives = new HashMap<String, Alternative>();
        maxCriteria = new HashMap<String, Double>();
    }

    public void addAlternative(String alternativeName, Alternative alternative) {

        alternatives.put(alternativeName, alternative);
    }

    public void calcAllCFs() {
        for (String alternativeKey: alternatives.keySet()) {
            alternatives.get(alternativeKey).calculateCF();
        }
    }

    public void calcMaxes() {
        for (String alternativeKey: alternatives.keySet()) {
            HashMap<String, Criterion> criteria = alternatives.get(alternativeKey).criteria;
            for (String key: criteria.keySet()) {
                if(maxCriteria.containsKey(key)){
                    if(maxCriteria.get(key) < criteria.get(key).value)
                        maxCriteria.put(key, criteria.get(key).value);
                }else{
                    maxCriteria.put(key, criteria.get(key).value);
                }
            }
        }
    }

    public void calcIndex() {
        for (Alternative alternative: alternatives.values()) {
            if(alternative.getAlternativeName().equalsIgnoreCase("C5"))
                System.out.println("here" );
            double index = 0;
            for (Criterion criterion:alternative.criteria.values()) {
                index += criterion.value/maxCriteria.get(criterion.resourceCode) * Weights.getInstance().getW(criterion.resourceCode);
            }
            alternative.setIndex(index);
        }
    }

//    public void calcAllCFs(){
//        for (Map.Entry<String, Alternative> alternativeList: alternatives.entrySet()) {
//            Alternative alternative = alternativeList.getValue();
//            alternative.CalculateCF();
//            for (HashMap.Entry<String, Double> criterion : alternative.criteria.entrySet()) {
//                String key = criterion.getKey();
//                Double value = criterion.getValue();
//                if (maxCriteria.containsKey(key) && maxCriteria.get(key) > abs(value)) {
//                    continue;
//                }
//                maxCriteria.put(key, value);
//            }
//        }
//    }

//    public void CalculateIndex() {
//        for (Map.Entry<String, Alternative> alternative: alternatives.entrySet()) {
//            double index = 0;
//            for (HashMap.Entry<String, Double> criterion : alternative.getValue().criteria.entrySet()) {
//                String key = criterion.getKey();
//                Double value = criterion.getValue();
//                Criterion weight = Weights.i().getW(key);
//                if(maxCriteria.get(key)==0)
//                    continue;
//                index += weight.value * weight.impact * value / maxCriteria.get(key);
//            }
//            alternative.getValue().setIndex(index);
//        }
//    }
}