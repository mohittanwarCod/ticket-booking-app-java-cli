package ticket.booking.services;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainService {

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String TRAINS_PATH = "app/src/main/java/ticket/booking/localDb/trains.json";
    private List<Train> trainList;

    public TrainService() throws IOException {
        try{
            File trains = new File(TRAINS_PATH);
            objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
            trainList = objectMapper.readValue(trains, new TypeReference<List<Train>>() {
            });
            System.out.println(trainList.size());
        }catch(IOException e){
           System.out.println("Trains file not found");
        }

    }


    public List<Train> searchTrains(String source, String destination) {
        List<Train> searchedTrain = trainList.stream().filter(train -> validTrain(train, source, destination))
                .collect(Collectors.toList());

        return searchedTrain;
    }

    private boolean validTrain(Train train, String source, String destination) {
        int sourceIndex = train.getStations().indexOf(source.toLowerCase());
        int destinationIndex = train.getStations().indexOf(destination.toLowerCase());

        return sourceIndex != -1 && destinationIndex != -1 && sourceIndex < destinationIndex;
    }


    public static void main(String[] args) {
        System.out.println("hello world");
        try{
            TrainService trainService = new TrainService();
            Train train = trainService.trainList.get(0);
            System.out.println(train);
        }catch(IOException e){
            System.out.println("Trains file not found");
            return;
        }



    }

    public void addTrain(Train newTrain) throws IOException {

        Optional<Train> exitstingTrain  = trainList.stream().filter(train->train.getTrainId().equalsIgnoreCase(newTrain.getTrainId())).findFirst();
        if(exitstingTrain.isPresent()){

            updateTrain(newTrain);
        }else{
            trainList.add(newTrain);
            saveTrainListToFile();
        }
    }

    public void updateTrain(Train updatedTrain) throws IOException {
        // Finding the index of train with sameId
        OptionalInt index  = IntStream.range(0,trainList.size()).filter(i -> trainList.get(i).getTrainId().equalsIgnoreCase(updatedTrain.getTrainId())).findFirst();

        if(index.isPresent()){
            System.out.println(index.getAsInt());
            trainList.set(index.getAsInt(), updatedTrain);
            saveTrainListToFile();
        }else{
            addTrain(updatedTrain);
        }

    }



    public void saveTrainListToFile() throws IOException {
        try{
            File trains = new File(TRAINS_PATH);
            objectMapper.writeValue(trains,trainList);
        }catch(IOException e){
            System.out.println("Trains file not found" + e.getMessage());
        }

    }







}
