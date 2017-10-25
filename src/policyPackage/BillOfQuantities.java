package policyPackage;

public class BillOfQuantities {
    //Perimeter in meteres
    private double landPerimeter;
    //Area in meteres
    private double landArea;
    //No of stories above ground including 0
    private double storiesUG;
    //No of stories below ground not including 0
    private double storiesAG;
    //percentage of the total land area with building permission usually 60%
    private double occupiedArea;

    private static BillOfQuantities instance = null;

    public static BillOfQuantities i() {
        if (instance == null) {
            instance = new BillOfQuantities();
        }
        return instance;
    }

    private BillOfQuantities() {
    }

    public double getLandPerimeter() {
        return landPerimeter;
    }

    public double getLandArea() {
        return landArea;
    }

    public double getStoriesUG() {
        return storiesUG;
    }

    public double getStoriesAG() {
        return storiesAG;
    }

    public double getOccupiedArea() {
        return occupiedArea;
    }

    public void setLandPerimeter(double landPerimeter) {
        this.landPerimeter = landPerimeter;
    }

    public void setLandArea(double landArea) {
        this.landArea = landArea;
    }

    public void setStoriesUG(double storiesUG) {
        this.storiesUG = storiesUG;
    }

    public void setStoriesAG(double storiesAG) {
        this.storiesAG = storiesAG;
    }

    public void setOccupiedArea(double occupiedArea) {
        this.occupiedArea = occupiedArea;
    }

    public void test() {
        System.out.println("landPerimeter = " + landPerimeter);
        System.out.println("landArea = " + landArea);
        System.out.println("storiesUG = " + storiesUG);
        System.out.println("storiesAG = " + storiesAG);
        System.out.println("occupiedArea = " + occupiedArea);
    }
}
