package policyPackage.resources;

import java.util.LinkedHashMap;

public class ResourceHandler {

    private static ResourceHandler instance = null;

    public static ResourceHandler i(){
        if(instance == null)
            instance = new ResourceHandler();
        return instance;
    }

    private LinkedHashMap<String, ShortEquipment> equipments = new LinkedHashMap<>();
    private LinkedHashMap<String, ShortMaterial> materials = new LinkedHashMap<>();

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
    public ShortEquipment getEQP(String equipmentCode) {
        return equipments.get(equipmentCode);
    }
}
