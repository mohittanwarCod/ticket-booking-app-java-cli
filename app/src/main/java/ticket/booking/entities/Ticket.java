package ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ticket {

    @JsonProperty("ticket_id")
    private String ticketId;

    @JsonProperty("user_id")
    private  String userId;

   @JsonProperty("source")
    private String source;

   @JsonProperty("destination")
    private String destination;

    @JsonProperty("date_of_travel")
    private Date dateOfTravel;

    @JsonProperty("train")
    private Train train;

    public Ticket(){

    };
    public Ticket(String ticketId, String userId, String source, String destination, Date dateOfTravel, Train train) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.source = source;
        this.destination = destination;
        this.dateOfTravel = dateOfTravel;
        this.train = train;
    }
    public void getTicketInfo(){
        System.out.println("Ticket ID: " + ticketId);
        System.out.println("source: " + source);
        System.out.println("destination: " + destination);
        System.out.println("date_of_travel: " + dateOfTravel);
        System.out.println("trainId: " + train.getTrainId());
    }
    public String getTicketId() {
        return ticketId;
    }


}
