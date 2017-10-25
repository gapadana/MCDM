package policyPackage;

/**
 * Created by aebra on 7/21/2017.
 */
public class Criterion
{
    public Double value;
    public Integer impact;
    public String name;
    public String resourceCode;

    public Criterion() {
//        this.value = value;
//        this.impact = impact;
    }

    public Criterion(Criterion criterion) {
        this.value = criterion.value;
        this.impact = criterion.impact;
        this.name = criterion.name;
        this.resourceCode = criterion.resourceCode;

    }

    public Criterion(String resourceCode, String name, double value) {
        this.resourceCode = resourceCode;
        this.name = name;
        this.value = value;
    }
}
