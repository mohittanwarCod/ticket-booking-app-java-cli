package ticket.booking.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Time;
import java.util.List;
import java.util.Map;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Train {
    @JsonProperty("train_id")
    private String trainId;

    @JsonProperty("train_no")
    private String trainNo;

    @JsonProperty("seats")
    private List<List<Integer>> seats;

    @JsonProperty("station_times")
    private Map<String, String> stationTimes;

    @JsonProperty("stations")
    private List<String> stations;

    public Train() {};
    public Train(String trainId, String trainNo, List<List<Integer>> seats, Map<String, String> stationTimes,List<String> stations) {
        this.trainId = trainId;
        this.trainNo = trainNo;
        this.seats = seats;
        this.stationTimes = stationTimes;

    }

    public String getTrainId(){
       return trainId;
    }

    public List<List<Integer>> getSeats(){
       return seats;
    }
    public Map<String, String> getStationTimes(){
        return stationTimes;
    }

    public Boolean setTrainSeatToOne(int row,int col){
        seats.get(row).set(col,1);
        return Boolean.TRUE;
    }

    public List<String> getStations(){
        return stations;
    }
    public void setSeats(List<List<Integer>> seats){
        this.seats = seats;
    }

    public String getTrainInfo(){
        return String.format("Train ID: %s TrainNo: %s",trainId,trainNo);
    }




}
