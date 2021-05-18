package Domain;

import java.io.Serializable;

public class Registration extends Entity<Integer> implements Serializable {
    private int riderID;
    private int raceID;

    public Registration(int riderID, int raceID) {
        this.riderID = riderID;
        this.raceID = raceID;
    }

    public int getRiderID() {
        return riderID;
    }

    public void setRiderID(int riderID) {
        this.riderID = riderID;
    }

    public int getRaceID() {
        return raceID;
    }

    public void setRaceID(int raceID) {
        this.raceID = raceID;
    }
}
