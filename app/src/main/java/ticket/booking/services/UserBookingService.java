package ticket.booking.services;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

//import org.objectweb.asm.TypeReference; // Correct package

import com.fasterxml.jackson.core.type.TypeReference; // ✅ Correct import

import ticket.booking.entities.Ticket;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.utils.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class UserBookingService {

    private User user;

    private List<User> userList;


    private ObjectMapper objectMapper = new ObjectMapper();


    private static final String USERS_PATH = "app/src/main/java/ticket/booking/localDb/users.json";

    // constructor -> if user is logged In
    public UserBookingService(User user1) throws IOException {

        this.user = user1;

        File users = new File(USERS_PATH);
//        System.out.println(users);

        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        userList = objectMapper.readValue(users,new TypeReference<List<User>> (){});
        System.out.println(userList.size());
        // TypeReference
    }
    public UserBookingService(){};

    public void saveUserListToFile() throws IOException {
        File userFile = new File(USERS_PATH);
        objectMapper.writeValue(userFile,userList);

    }

    public Boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter(user1-> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(),user1.getHashedPassword());
        }).findFirst();
        System.out.println(foundUser.isPresent());
        if(foundUser.isPresent()==Boolean.TRUE){
            user = foundUser.get();
        }
                return foundUser.isPresent();


    }

    public Boolean signUp(User user1){
        try{
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;

        }catch(IOException e){
            return Boolean.FALSE;

        }
    }

    // TODO: complete the logic
    public boolean bookTrainSeat (Train train,int row,int col) throws IOException {
        // TODO: CHANGE FUNCTION
//        train.setTrainSeatToOne(row,col);

        try{

            /* Printing the seats for testing*/
//            for(List<Integer> i:train.getSeats()){
//                for(Integer seat:i){
//                    System.out.print(seat+" ");
//                }
//                System.out.println();
//            }
            Boolean isUserLoggedIn = loginUser();
            if(isUserLoggedIn==Boolean.FALSE){
                System.out.println("Login First");
                return false;
            }
            TrainService trainService = new TrainService();
            List<List<Integer>> seats = train.getSeats();

            // Checking the valid conditions is seats present
            if(row>=0 && row<seats.size() && col>=0 && col<seats.get(row).size()){
              if(seats.get(row).get(col)==1){
                  // seat is already booked
                  System.out.println("Seat is Already Booked");
                  return false;
              }
              seats.get(row).set(col,1);
              train.setSeats(seats);
              trainService.addTrain(train);
              updateUserTickets(train);
              return true;


            }else{

                return false;
            }

        }catch(IOException e){
            System.out.println(e.getMessage());
            return Boolean.FALSE;
        }


    }
    public boolean cancelTicket(String ticketId) throws IOException {
        boolean isUserLoggedIn = loginUser();
        if(isUserLoggedIn==Boolean.FALSE){
            System.out.println("Login First");
            return false;
        }
        List<Ticket> bookedTicket = user.getTicketsBooked();
        List<Ticket> updatedTickets = bookedTicket.stream()
                .filter(ticket1 -> !ticket1.getTicketId().equals(ticketId))
                .collect(Collectors.toList());
        user.setTicketsBooked(updatedTickets);
        saveUserListToFile();
        return Boolean.TRUE;

    }
   public void updateUserTickets(Train train) throws IOException {
        List<String> trainStations = train.getStations();
        String source = trainStations.get(0);
        String destination = trainStations.get(trainStations.size()-1);
        Date dateOFTravel = new Date(System.currentTimeMillis());
        Ticket ticket = new Ticket(UUID.randomUUID().toString(),user.getUserId().toString(),source,destination,dateOFTravel,train);
        System.out.println(user.getUserId().toString());
        List<Ticket> oldTicketsBooked = user.getTicketsBooked();
        oldTicketsBooked.add(ticket);
        user.setTicketsBooked(oldTicketsBooked);
        saveUserListToFile();
   }
    public void fetchBookings(){
        Optional<User> userFetched = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(),user1.getHashedPassword());
        }).findFirst();

        if(userFetched.isPresent()){
            userFetched.get().printTickets();
        }

    }

    public List<List<Integer>> fetchSeats(Train train){
        return train.getSeats();
    }

    public List<Train> getTrains(String source, String destination){

      try{
          TrainService trainService = new TrainService();
          return trainService.searchTrains(source,destination);

      }catch(IOException err){
         return  new ArrayList<>();
      }
    }





}
