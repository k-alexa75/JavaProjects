package Domain;

import java.io.Serializable;

public class Race extends Entity<Integer> implements Serializable {

    private String name;
    private int engineCapacity;
    private int maximumRiders;

    public Race(String name, int engineCapacity, int maximumRiders) {
        this.name = name;
        this.engineCapacity = engineCapacity;
        this.maximumRiders = maximumRiders;
    }

    public Race(String name, int engineCapacity) {
        this.name = name;
        this.engineCapacity = engineCapacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(int engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    public int getMaximumRiders() {
        return maximumRiders;
    }

    public void setMaximumRiders(int maximumRiders) {
        this.maximumRiders = maximumRiders;
    }

    @Override
    public String toString() {
        return "Race{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", engineCapacity=" + engineCapacity +
                ", maximumRiders=" + maximumRiders +
                '}';
    }
}
