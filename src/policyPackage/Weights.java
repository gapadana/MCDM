package policyPackage;

import java.util.LinkedHashMap;

public class Weights {
    // TODO: 7/21/2017 +/- of criteria
    private LinkedHashMap<String, Double> weights;

    private static Weights instance = null;

    public static final Weights getInstance() {
        if (instance == null) {
            instance = new Weights();
        }
        return instance;
    }

    private Weights() {
        weights = new LinkedHashMap<>();
    }

    public void print(String string) {
        System.out.println(string);
    }

    public double getW(String key) {
        return weights.get(key);
    }

    public boolean hasKey(String key){
        return weights.containsKey(key);
    }

    public void addWeight(String criteria, int selectedItem, boolean minimized) {
        double value = selectedItem;
        weights.put(criteria, value * (minimized ? -1 : 1));
    }
}

