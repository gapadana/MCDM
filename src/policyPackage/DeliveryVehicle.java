package policyPackage;

/**
 * Created by aebra on 7/28/2017.
 */
public class DeliveryVehicle {
    private String name;
    private double capacity;
    private double enginePower;

    public DeliveryVehicle(String name, double capacity, double enginePower) {
        this.name = name;
        this.capacity = capacity;
        this.enginePower = enginePower;
    }

    public String getName() {
        return name;
    }

    public double getCapacity() {
        return capacity;
    }

    public double getEnginePower() {
        return enginePower;
    }

    //    public double getEmissionFactor(double loadAmount, double distance) {
//        // TODO: 7/28/2017 add assumption to GUI
//        double truckNumber = loadAmount / capacity;
//        double h = distance * 2 * truckNumber / Constants.AVERAGE_SPEED;
//        return enginePower * Constants.DELIVERY_VEHICLE_CO2 * h;
//    }
}
