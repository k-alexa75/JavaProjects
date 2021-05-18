package objectprotocol;

import Domain.Rider;

import java.util.List;

public class GetRidersByTeamResponse implements Response{
    private List<Rider> riders;

    public GetRidersByTeamResponse(List<Rider> riders) {
        this.riders = riders;
    }

    public List<Rider> getRiders() {
        return riders;
    }
}
