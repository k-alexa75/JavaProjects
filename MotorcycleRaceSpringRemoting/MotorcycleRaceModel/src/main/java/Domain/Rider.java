package Domain;

import java.io.Serializable;

public class Rider extends Entity<Integer> implements Serializable {
    private String firstName;
    private String lastName;
    private String team;
    private int engineCapacity;

    public Rider(String firstName, String lastName, String team, int engineCapacity) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.team = team;
        this.engineCapacity = engineCapacity;
    }

    public Rider(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getTeam() {
        return team;
    }

    public int getEngineCapacity() {
        return engineCapacity;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setEngineCapacity(int engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    @Override
    public String toString() {
        return "Rider{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", team='" + team + '\'' +
                ", engineCapacity=" + engineCapacity +
                '}';
    }
}
