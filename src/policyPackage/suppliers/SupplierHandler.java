package policyPackage.suppliers;

import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.Set;

public class SupplierHandler {

    private LinkedHashMap <String, HashSet<Supplier>> suppliers = new LinkedHashMap<>();
    private LinkedHashMap <String, Supplier> selectedSuppliers = new LinkedHashMap<>();

    public static SupplierHandler i(){
        if(instance == null)
            instance = new SupplierHandler();
        return instance;
    }

    private static SupplierHandler instance = null;

    private SupplierHandler() {}

    public void addSupplier(Supplier supplier){
        if(!suppliers.containsKey(supplier.getMaterialCode())) {
            suppliers.put(supplier.getMaterialCode(), new HashSet<Supplier>());
        }
        suppliers.get(supplier.getMaterialCode()).add(supplier);
    }

    public Set<String> getSupplierMTLs(){
        return suppliers.keySet();
    }

    public HashSet<Supplier> getAvailableSuppliers(String materialCode) {
        return suppliers.get(materialCode);
    }

    public void setSelectedSupplier(String mtlCode, String supplierName){
        for (Supplier supplier : suppliers.get(mtlCode)) {
            if(supplier.getSupplier().equalsIgnoreCase(supplierName)){
                selectedSuppliers.put(mtlCode, supplier);
            }
        }
    }

    public Supplier getSelectedSupplier(String mtlCode){
        return selectedSuppliers.get(mtlCode);
    }
}
