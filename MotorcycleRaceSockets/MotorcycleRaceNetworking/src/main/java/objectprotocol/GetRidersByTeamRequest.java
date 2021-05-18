package objectprotocol;

public class GetRidersByTeamRequest implements Request{
    private String team;

    public GetRidersByTeamRequest(String team) {
        this.team = team;
    }

    public String getTeam() {
        return team;
    }
}
