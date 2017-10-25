//Source file: D:\\Users\\z3342147\\Desktop\\New folder\\java\\Aggregator.java
package policyPackage;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import structurePackage.*;

public class Aggregator {


    public static final String ANSI_RED = "\t";
    public static final String ANSI_RESET = "";
//    private HashMap<String, Element> elements;

//    public static void main(String[] args) {
//        new Aggregator();
//    }

    public Aggregator(LinkedHashMap<String, Element> elements) {
        for (String elementKey : elements.keySet()) {
            elements.get(elementKey).calcAllCFs();
            elements.get(elementKey).calcMaxes();
            elements.get(elementKey).calcIndex();
        }
    }

    public void SelectMaxCMindex() {

    }

    private double findMax(ArrayList<Double> list) {
        double max = 0;
        for (Double doubleV : list) {
            if (doubleV > max)
                max = doubleV;
        }
        return max;
    }
}
