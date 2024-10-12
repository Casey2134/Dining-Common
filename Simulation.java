import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Simulation {
    // variables
    double currentTime = 0;
    double startTime;
    double nextArrivalTime;
    double nextEndEntranceServiceTime;
    double nextEndServiceTime;
    double nextEndOrderServiceTime;
    // SingleServerQueue objects
    SingleServerQueue entrance = new SingleServerQueue();

    // SingleServerQueue objects

    SingleServerQueue[] entrances = new SingleServerQueue[]{entrance};

    // Station objects
    Station station1 = new Station("Kalamata Leaf", 0, 6, 9);
    Station station2 = new Station("Bamboo Bowl", 0, 5, 6);
    Station station3 = new Station("Smoke 'N Flames", 0, 2, 13);
    Station station4 = new Station("OH! yOU Cookin?", 4, 4, 7);
    Station station5 = new Station("Salad Bar", 0, 1, 15);
    Station station6 = new Station("Nothing But Desserts", 0, 3, 3);
    // Station Array
    Station[] stations = new Station[]{station1, station2, station3, station4, station5, station6};
    // ArrivalProcess Object
    ArrivalProcess arrivalProcess = new ArrivalProcess();
    // CompletedJobs Queue
    CompletedJobs completedJobs = new CompletedJobs();

    // runs simulation
    public void run(double simTime, double startTime) {
        this.startTime = startTime;
        // adds first job as soon as sim starts
        entrance.add(arrivalProcess.nextJob(currentTime), currentTime);
        // begins simulation loop
        while (currentTime < simTime) {
            doLoop();
        }
        // process data
        // average service time
        double averageServiceTime = 0;
        double totalJobs = 0;
        Job job;
        while ((job = completedJobs.get()) != null) {
            averageServiceTime += job.getServiceTime();
            totalJobs++;
        }
        averageServiceTime = averageServiceTime / totalJobs;
        System.out.println("Average service time: " + averageServiceTime);
        System.out.println("Total completed jobs: " + totalJobs);
        System.out.println("Jobs per unit of time: " + totalJobs / simTime);
        System.out.println("Total time: " + simTime);
    }

    // adds a job to the server with the smallest queue
    private void addJob(Job job, double currentTime, double startTime) {
        //Selecting Food Station
        Random random = new Random();
        GetProbabilities probabilityMaker = new GetProbabilities();
        double[] probabilities = probabilityMaker.getStationProb(6);
        for(int i = 0 ; i < stations.length ; i++){
            double divideNumber = 1.0 + (stations[i].getOrderQueueLenth() / 20);
                double addToOthers = probabilities[i] / (5 * divideNumber);
                probabilities[i] /= divideNumber;
                for(int j = 0 ; j < stations.length ; j++){
                    if(j != i){
                        probabilities[j] += addToOthers;
                    }
                }
        }
        int number = getProbabilityIndex(probabilities);
        stations[number].addJob(job, currentTime);
        //Selecting Food Item
        double foodTime = currentTime + startTime;
        Food[] availableFood;
        if(foodTime > 16.5){
            availableFood = stations[number].getDinner();
        } else if(foodTime > 11 && foodTime < 14.5){
            availableFood = stations[number].getLunch();
        } else if(foodTime < 10.5){
            availableFood = stations[number].getBreakfast();
        } else{
            availableFood = null;
        }
        double[] foodProbabilities = probabilityMaker.getStationProb(availableFood.length);
        job.setFood(availableFood[getProbabilityIndex(foodProbabilities)]);
    }

    private int getProbabilityIndex(double[] probabilities){
        Random random = new Random();
        double number = random.nextDouble(100);
        for(int i = 0 ; i < probabilities.length ; i++){
            if(number > probabilities[i]){
                probabilities[i] = 0;
            }
        }
        int probabilityNumber = 100;
        double lowestProbability = 100;
        for(int i = 0 ; i < probabilities.length ; i++){
            if(probabilities[i] > 0){
                if(lowestProbability > probabilities[i]){
                    probabilityNumber = i;
                }
            }
        }
        return(probabilityNumber);
    }

    // gets the service time closest to currnent time
    private double getNextEndServiceTime() {
        double nextEndServiceTime = Double.MAX_VALUE;
        for (int i = 0; i < stations.length; i++) {
            if (stations[i].getEndServiceTime() < nextEndServiceTime) {
                nextEndServiceTime = stations[i].getEndServiceTime();
            }

        }
        return nextEndServiceTime;
    }

    private double getNextEndOrderServiceTime() {
        double nextEndServiceTime = Double.MAX_VALUE;
        for (int i = 0; i < stations.length; i++) {
            if (stations[i].orderQueueGetEndServiceTime() < nextEndServiceTime) {
                nextEndServiceTime = stations[i].orderQueueGetEndServiceTime();
            }

        }
        return nextEndServiceTime;
    }
    // gets the end of entrance time
    private double getNextEndEntranceServiceTime() {
        double nextEndServiceTime = Double.MAX_VALUE;
        for (int i = 0; i < entrances.length; i++) {
            if (entrances[i].getEndServiceTime() < nextEndServiceTime) {
                nextEndServiceTime = entrances[i].getEndServiceTime();
            }

        }
        return nextEndServiceTime;
    }

    // returns the server that completes the next job
    private Station getNextEndServer() {
        // will point to server with the next task to end
        Station nextEndServer = stations[0];
        for (int i = 1; i < stations.length; i++) {
            if (stations[i].getEndServiceTime() < nextEndServer.getEndServiceTime()) {
                nextEndServer = stations[i];
            }
        }
        return nextEndServer;
    }

    // simulation loop
    private void doLoop() {
        //transferToPickup(currentTime)
        if(currentTime == nextArrivalTime){
            entrance.add(arrivalProcess.nextJob(currentTime), currentTime);
            nextArrivalTime = currentTime + arrivalProcess.nextArrivalTime();
        } else if(currentTime == nextEndEntranceServiceTime){
            addJob(entrance.dequeue(currentTime), currentTime, startTime);
            nextEndEntranceServiceTime = getNextEndEntranceServiceTime();
        } else if(currentTime == nextEndServiceTime){
            //End of Station
            //Will add seconds here later
            completedJobs.add(getNextEndServer().completeJob(currentTime));
            nextEndServiceTime = getNextEndServiceTime();
        } else if(currentTime == nextEndOrderServiceTime){
            getNextEndServer().transferToPickup(currentTime);
            nextEndOrderServiceTime = getNextEndOrderServiceTime();
        }
        double[] endServiceTimes = new double[]{nextEndEntranceServiceTime, nextEndOrderServiceTime, nextEndServiceTime};
        double closestTime = endServiceTimes[0];
        for(int i = 1 ; i < endServiceTimes.length ; i++){
            if(closestTime > endServiceTimes[i]){
                closestTime = endServiceTimes[i];
            }
        }
        currentTime = closestTime;

    }
    private boolean printResults(Job job){
        boolean result = true;
        File file = new File("src/Results.java");
        FileWriter writer;
        try {
            writer = new FileWriter(file, true);
            writer.append(job.toString()).append("\n");
            writer.close();
        }catch(IOException error){
            error.printStackTrace();
            result = false;
        }
        return(result);
    }
}
