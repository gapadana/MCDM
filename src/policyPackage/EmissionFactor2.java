package policyPackage;

//Source file: D:\\Users\\z3342147\\Desktop\\New folder\\java\\EmissionFactor2.java


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class EmissionFactor2 {

    // TODO: 7/21/2017 getEF EmissionFactor2 from config file
    private HashMap<String, Emission> emissions;
    private static EmissionFactor2 instance = null;

    public static final EmissionFactor2 getInstance() {
        if (instance == null) {
            instance = new EmissionFactor2();
        }
        return instance;
    }

    private EmissionFactor2() {

        intializeEF();
    }

    private void intializeEF() {
        Properties properties = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("./config.properties");
        } catch (FileNotFoundException e) {
            // TODO: 7/28/2017 return error for GUI
            System.out.println("could not find the file");
            e.printStackTrace();
        }
        try {
            properties.load(fis);
            if (fis != null) {
                fis.close();
            }
        } catch (IOException e) {
            // TODO: 7/28/2017 return error for GUI
            System.out.println("could not open file");
            e.printStackTrace();
        }

        String emS = "";
        this.emissions = new HashMap<String, Emission>();
        try {
            for(Map.Entry<Object, Object> property: properties.entrySet()){
                emS = String.valueOf(property.getKey());
                String[] split = String.valueOf(property.getValue()).trim().split(",");
                String value = split[0];
                String unit = split[1];
                Emission emission = new Emission(emS.trim(), unit, Double.parseDouble(value));
                emissions.put(emS.toLowerCase(), emission);
            }
        } catch (NumberFormatException e) {
            // TODO: 7/28/2017 return error for GUI
            System.out.println("config file problem in \"" + emS + "\"");
        }
    }

    public Emission getEF(String emissionFactorName) {
        emissionFactorName = emissionFactorName.toLowerCase();
        try {
            return emissions.get(emissionFactorName);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: 7/21/2017 error
            return null;
        }
    }

    public HashMap<String, Emission> getAllEF() {
        return emissions;
    }

    public void changeEFValue(String emissionName, double value) {
        emissions.get(emissionName.toLowerCase()).changeValue(value);
    }

    public void changeEFUnit(String emissionName, String unit) {
        emissions.get(emissionName.toLowerCase()).changeUnit(unit);
    }

    public void changeEFName(String emissionName, String newValue) {
        Emission emission = emissions.get(emissionName.toLowerCase());
        emissions.remove(emissionName);
        emission.changeName(newValue);
        emissions.put(newValue.toLowerCase(), emission);
    }
}
