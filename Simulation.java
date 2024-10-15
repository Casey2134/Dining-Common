import java.util.Random;

public class Simulation {
    // variables
    double currentTime = 0;
    double nextArrivalTime;
    double nextEndServiceTime;
    double nextEndOrderTime;
    // SingleServerQueue objects
    SingleServerQueue server1 = new SingleServerQueue();
    SingleServerQueue server2 = new SingleServerQueue();
    // SingleServerQueue Array
    SingleServerQueue[] servers = new SingleServerQueue[] { server1, server2 };
    // ArrivalProcess Object
    ArrivalProcess arrivalProcess = new ArrivalProcess();
    // CompletedJobs Queue
    CompletedJobs completedJobs = new CompletedJobs();
    // Station objects
    Station station1 = new Station("Kalamata Leaf", 0, 6, 9);
    Station station2 = new Station("Bamboo Bowl", 0, 5, 6);
    Station station3 = new Station("Smoke 'N Flames", 0, 2, 13);
    Station station4 = new Station("OH! yOU Cookin?", 4, 4, 7);
    Station station5 = new Station("Salad Bar", 0, 1, 15);
    Station station6 = new Station("Nothing But Desserts", 0, 3, 3);
    // Station Array
    Station[] stations = new Station[] { station1, station2, station3, station4, station5, station6 };

    // runs simulation
    public void run(double simTime) {
        // adds first job as soon as sim starts
        addJob(arrivalProcess.nextJob(currentTime), currentTime);
        nextEndOrderTime = getNextEndOrderTime();
        nextEndServiceTime = getNextEndServiceTime();
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
    private void addJob(Job job, double currentTime) {
        int minJobs = 0;
        int minQueueLength = stations[0].getOrderQueueLenth();
        // iterating across the array of servers
        for (int i = 0; i < stations.length; i++) {
            if (stations[i].getOrderQueueLenth() < minQueueLength) {
                minJobs = i;
            }
            stations[minJobs].addJob(job, currentTime);
        }
        // GetProbabilities probabilityMaker = new GetProbabilities();
        // double[] probabilities = probabilityMaker.getStationProb(6);
        // for (int i = 0; i < stations.length; i++) {
        // double divideNumber = 1.0 + ((float) stations[i].getOrderQueueLenth() / 20);
        // double addToOthers = probabilities[i] / (5 * divideNumber);
        // probabilities[i] /= divideNumber;
        // for (int j = 0; j < stations.length; j++) {
        // if (j != i) {
        // probabilities[j] += addToOthers;
        // }
        // }
        // }
        // int number = getProbabilityIndex(probabilities);
        // stations[number].addJob(job, currentTime);
    }

    private int getProbabilityIndex(double[] probabilities) {
        Random random = new Random();
        double number = random.nextDouble(100);
        for (int i = 0; i < probabilities.length; i++) {
            if (number > probabilities[i]) {
                probabilities[i] = 0;
            }
        }
        int probabilityNumber = 0;
        double lowestProbability = 100;
        for (int i = 0; i < probabilities.length; i++) {
            if (probabilities[i] > 0) {
                if (lowestProbability > probabilities[i]) {
                    probabilityNumber = i;
                }
            }
        }
        return (probabilityNumber);
    }

    // gets the service time closest to current time
    private double getNextEndOrderTime() {
        double nextEndServiceTime = Double.MAX_VALUE;
        for (int i = 0; i < stations.length; i++) {
            if (stations[i].orderQueueGetEndServiceTime() < nextEndServiceTime) {
                nextEndServiceTime = stations[i].orderQueueGetEndServiceTime();
            }

        }
        return nextEndServiceTime;
    }

    private double getNextEndServiceTime() {
        double nextEndServiceTime = Double.MAX_VALUE;
        for (int i = 0; i < stations.length; i++) {
            if (stations[i].getEndServiceTime() < nextEndServiceTime) {
                nextEndServiceTime = stations[i].getEndServiceTime();
            }

        }
        return nextEndServiceTime;
    }

    // returns the server that completes the next job
    private Station getNextEndOrder() {
        // will point to server with the next task to end
        Station nextEndServer = stations[0];
        for (int i = 0; i < stations.length; i++) {
            if (stations[i].orderQueueGetEndServiceTime() < nextEndServer.orderQueueGetEndServiceTime()) {
                nextEndServer = stations[i];
            }
        }
        return nextEndServer;
    }

    private Station getNextEndServer() {
        Station nextEndServer = stations[0];
        for (int i = 0; i < stations.length; i++) {
            if (stations[i].getEndServiceTime() < nextEndServer.getEndServiceTime()) {
                nextEndServer = stations[i];
            }
        }
        return nextEndServer;
    }

    private double getNextEvent() {
        if (nextArrivalTime < nextEndServiceTime && nextArrivalTime < nextEndOrderTime) {
            return nextArrivalTime;
        } else if (nextEndOrderTime < nextArrivalTime && nextEndOrderTime < nextEndServiceTime) {
            return nextEndOrderTime;
        } else {
            return nextEndServiceTime;
        }

    }

    // simulation loop
    private void doLoop() {
        if (currentTime == nextArrivalTime) {
            addJob(arrivalProcess.nextJob(currentTime), currentTime);
            nextArrivalTime = currentTime + arrivalProcess.nextArrivalTime();
        } else if (currentTime == nextEndServiceTime) {
            completedJobs.add(getNextEndServer().completeJob(currentTime));
            nextEndServiceTime = currentTime + getNextEndServiceTime();
        } else if (currentTime == nextEndOrderTime) {
            getNextEndOrder().transferToPickup(currentTime);
            nextEndOrderTime = currentTime + getNextEndOrderTime();
            nextEndServiceTime = currentTime + getNextEndServiceTime();
        }
        currentTime = getNextEvent();

    }

}