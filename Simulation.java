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

    // Station objects
    Station station1 = new Station("Kalamata Leaf", 0, 6, 9);
    Station station2 = new Station("Bamboo Bowl", 0, 5, 6);
    Station station3 = new Station("Smoke 'N Flames", 0, 2, 13);
    Station station4 = new Station("OH! yOU Cookin?", 4, 4, 7);
    Station station5 = new Station("Salad Bar", 0, 1, 15);
    Station station6 = new Station("Nothing But Desserts", 0, 3, 3);
    // Station Array
    Station[] stations = new Station[] { station1, station2, station3, station4, station5, station6 };
    // ArrivalProcess Object
    ArrivalProcess arrivalProcess = new ArrivalProcess();
    // CompletedJobs Queue
    CompletedJobs completedJobs = new CompletedJobs();

    // runs simulation
    public void run(double simTime, double startTime) {
        this.startTime = startTime;
        // adds first job as soon as sim starts
        entrance.add(arrivalProcess.nextJob(currentTime), currentTime);
        // begins simulation loop\
        nextArrivalTime = arrivalProcess.nextArrivalTime();
        nextEndEntranceServiceTime = entrance.getEndServiceTime();
        nextEndServiceTime = getNextEndServiceTime();
        nextEndOrderServiceTime = getNextEndOrderServiceTime();
        while (currentTime < simTime) {
            doLoop();
        }
        // process data
        // average service time
        double averageServiceTime = 0;
        double totalJobs = 0;
        Job job;
        while ((job = completedJobs.get()) != null) {
            // printResults(job);
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
        // Selecting Food Station
        GetProbabilities probabilityMaker = new GetProbabilities();
        double[] probabilities = probabilityMaker.getStationProb(6);
        for (int i = 0; i < stations.length; i++) {
            if(stations[i].getOrderQueueLenth() > 1) {
                probabilities[i] /= stations[i].getOrderQueueLenth();
                double addToOthers = (probabilities[i] * (stations[i].getOrderQueueLenth() - 1)) / (stations.length - 1);
                for (int j = 0; j < stations.length; j++) {
                    if (j != i) {
                        probabilities[j] += addToOthers;
                    }
                }
            }
        }
        int number = getProbabilityIndex(probabilities);
        stations[number].addJob(job, currentTime);
        nextEndOrderServiceTime = getNextEndOrderServiceTime();
        // Selecting Food Item
        double foodTime = currentTime + startTime;
        Food[] availableFood;
        if (foodTime > 16.5) {
            availableFood = stations[number].getDinner();
        } else if (foodTime > 11 && foodTime < 14.5) {
            availableFood = stations[number].getLunch();
        } else if (foodTime < 10.5) {
            availableFood = stations[number].getBreakfast();
        } else {
            availableFood = new Food[0];
        }
        double[] foodProbabilities = probabilityMaker.getStationProb(availableFood.length);
        int index = getProbabilityIndex(foodProbabilities);
        if (index > -1) {
            job.setFood(availableFood.length != 0 ? availableFood[index] : null);
        }
    }

    private int getProbabilityIndex(double[] probabilities) {
        Random random = new Random();
        double number = random.nextDouble(100);
        double currentNumber = 0;
        for(int i = 0 ; i < probabilities.length ; i++){
            if(number >= currentNumber && number < (currentNumber + probabilities[i])){
                return(i);
            }
            currentNumber += probabilities[i];
        }
        return(-1);
    }

    // gets the service time closest to current time
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
        double nextEndServiceTime = stations[0].orderQueueGetEndServiceTime();
        for (int i = 1; i < stations.length; i++) {
            if (stations[i].orderQueueGetEndServiceTime() < nextEndServiceTime) {
                nextEndServiceTime = stations[i].orderQueueGetEndServiceTime();
            }
        }
        return nextEndServiceTime;
    }

    // gets the end of entrance time
    private double getNextEndEntranceServiceTime() {
        return entrance.getEndServiceTime();
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
        //System.out.println(entrance.length());
        //System.out.println(station1.getEndServiceTime());
        //System.out.println(station2.getPickupQueueLenth());
        //System.out.println(nextEndOrderServiceTime);
        if (currentTime == nextArrivalTime) {
            entrance.add(arrivalProcess.nextJob(currentTime), currentTime);
            nextArrivalTime = currentTime + arrivalProcess.nextArrivalTime();
            nextEndEntranceServiceTime = getNextEndEntranceServiceTime();
        } else if (currentTime == nextEndEntranceServiceTime) {
            addJob(entrance.dequeue(currentTime), currentTime, startTime);
            nextEndEntranceServiceTime = getNextEndEntranceServiceTime();
        } else if (currentTime == nextEndServiceTime) {
            System.out.println("Hi");
            // End of Station
            // Will add seconds here later
            completedJobs.add(getNextEndServer().completeJob(currentTime));
            nextEndServiceTime = getNextEndServiceTime();
        } else if (currentTime == nextEndOrderServiceTime) {
            System.out.println("hi");
            getNextEndServer().transferToPickup(currentTime);
            nextEndOrderServiceTime = getNextEndOrderServiceTime();
        }
        //nextEndEntranceServiceTime = getNextEndEntranceServiceTime();
        //nextEndServiceTime = getNextEndServiceTime();
        //nextEndOrderServiceTime = getNextEndOrderServiceTime();

        double[] endServiceTimes = new double[] { nextEndEntranceServiceTime, nextEndOrderServiceTime,
                nextEndServiceTime, nextArrivalTime};
        //System.out.printf("%f | %f%n", nextArrivalTime, nextEndEntranceServiceTime);
        double closestTime = endServiceTimes[0];
        for (int i = 1; i < endServiceTimes.length; i++) {
            if (closestTime > endServiceTimes[i] && endServiceTimes[i] != currentTime) {
                closestTime = endServiceTimes[i];
            }
        }
        currentTime = closestTime;

    }

    private void printResults(Job job) {
        File file = new File("Results.java");
        FileWriter writer;
        try {
            writer = new FileWriter(file, true);
            writer.append(job.toString()).append("\n");
            writer.close();
        } catch (IOException error) {
            error.printStackTrace();
        }
    }
}
