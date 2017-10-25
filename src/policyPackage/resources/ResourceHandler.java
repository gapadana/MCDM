package policyPackage.resources;

import java.util.HashMap;

public class ResourceHandler {

    private static ResourceHandler instance = null;

    public static ResourceHandler i(){
        if(instance == null)
            instance = new ResourceHandler();
        return instance;
    }

    private HashMap<String, ShortEquipment> equipments = new HashMap<>();
    private HashMap<String, ShortMaterial> materials = new HashMap<>();

    public boolean hasMTL(String code){
        return materials.containsKey(code);
    }

    public boolean hasEQP(String code){
        return equipments.containsKey(code);
    }

    public void addEQP(Equipment equipment){
        if(!hasEQP(equipment.getCode())){
            ShortEquipment shortEquipment = new ShortEquipment(
                    equipment.getName(),
                    equipment.getCode(),
                    equipment.getUnit(),
                    equipment.getEnergySource(),
                    equipment.getPower());
            equipments.put(equipment.getCode(), shortEquipment);
        }
    }

    public void addMTL(Material material){
        if(!hasMTL(material.getCode())){
            ShortMaterial shortMaterial = new ShortMaterial(
                    material.getName(),
                    material.getCode(),
                    material.getUnit(),
                    material.getDescription()
            );
            materials.put(material.getCode(), shortMaterial);
        }
    }

    public ShortMaterial getMTL(String materialCode) {
        return materials.get(materialCode);
    }
}
