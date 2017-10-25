package policyPackage.emissionFactors;

import java.util.HashMap;

public class EmissionFactorHandler {

    private static EmissionFactorHandler instance = null;

    public static EmissionFactorHandler i(){
        if(instance == null)
            instance = new EmissionFactorHandler();
        return instance;
    }

    private EmissionFactorHandler(){}

    private HashMap<String, HashMap<String, EmissionFactor>> efs = new HashMap<>();

    public void addEmissionFactor(String code, EmissionFactor emissionFactor){
        HashMap<String, EmissionFactor> map = new HashMap<>();
        if(efs.containsKey(code)) {
            map = efs.get(code);
        }
        map.put(emissionFactor.getName(), emissionFactor.newInstance());
        efs.put(code,map);
    }

    public EmissionFactor getEf(String code) {
        HashMap<String, EmissionFactor> resourceEFs = efs.get(code);
        for (String key: resourceEFs.keySet()) {
            if(key.toLowerCase().contains("co2"))
                return resourceEFs.get(key);
        }
        return null;
    }
}
