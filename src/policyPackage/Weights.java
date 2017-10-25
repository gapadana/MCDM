package policyPackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Weights {
    // TODO: 7/21/2017 +/- of criteria
    private HashMap<String, Double> weights;

    private static Weights instance = null;

    public static final Weights getInstance() {
        if (instance == null) {
            instance = new Weights();
        }
        return instance;
    }

    private Weights() {
        weights = new HashMap<>();
    }

    public void print(String string) {
        System.out.println(string);
    }

    public double getW(String key) {
        return weights.get(key);
    }

    public void addWeight(String criteria, int selectedItem, boolean minimized) {
        double value = selectedItem;
        weights.put(criteria, value * (minimized ? -1 : 1));
    }
}

