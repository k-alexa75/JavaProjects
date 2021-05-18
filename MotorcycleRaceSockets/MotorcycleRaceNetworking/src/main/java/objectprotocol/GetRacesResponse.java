package objectprotocol;

import Domain.RowData;

import java.util.List;

public class GetRacesResponse implements Response{
    List<RowData> races;

    public GetRacesResponse(List<RowData> races) {
        this.races = races;
    }

    public List<RowData> getRaces() {
        return races;
    }
}
