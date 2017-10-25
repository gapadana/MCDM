package policyPackage.emissionFactors;

import java.util.LinkedHashMap;

public class EmissionFactorHandler {

    private static EmissionFactorHandler instance = null;

    public static EmissionFactorHandler i(){
        if(instance == null)
            instance = new EmissionFactorHandler();
        return instance;
    }

    private EmissionFactorHandler(){}

    private LinkedHashMap<String, LinkedHashMap<String, EmissionFactor>> efs = new LinkedHashMap<>();

    public void addEmissionFactor(String code, EmissionFactor emissionFactor){
        LinkedHashMap<String, EmissionFactor> map = new LinkedHashMap<>();
        if(efs.containsKey(code)) {
            map = efs.get(code);
        }
        map.put(emissionFactor.getName(), emissionFactor.newInstance());
        efs.put(code,map);
    }

    public EmissionFactor getEf(String code) {
        LinkedHashMap<String, EmissionFactor> resourceEFs = efs.get(code);
        for (String key: resourceEFs.keySet()) {
            if(key.toLowerCase().contains("co2"))
                return resourceEFs.get(key);
        }
        return null;
    }
}
