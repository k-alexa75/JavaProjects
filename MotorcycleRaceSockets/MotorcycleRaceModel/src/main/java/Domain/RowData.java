package Domain;

import java.io.Serializable;

public class RowData implements Serializable {

    private Race race;
    private int riders;

    public RowData(Race race, int riders) {
        this.race = race;
        this.riders = riders;
    }

    public Race getRace() {
        return race;
    }

    public int getRiders() {
        return riders;
    }
}
