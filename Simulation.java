public class Simulation {
    // variables
    double currentTime = 0;
    double nextArrivalTime;
    double nextEndServiceTime;
    // SingleServerQueue objects
    SingleServerQueue server1 = new SingleServerQueue();
    SingleServerQueue server2 = new SingleServerQueue();
    // SingleServerQueue Array
    SingleServerQueue[] servers = new SingleServerQueue[] { server1, server2 };
    // ArrivalProcess Object
    ArrivalProcess arrivalProcess = new ArrivalProcess();
    // CompletedJobs Queue
    CompletedJobs completedJobs = new CompletedJobs();

    // runs simulation
    public void run(double simTime) {
        // adds first job as soon as sim starts
        addJob(arrivalProcess.nextJob(currentTime), currentTime);
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
        int minJobs = servers[1].length();
        // iterating across the array of servers
        for (int i = 0; i < servers.length; i++) {
            if (servers[i].length() < minJobs) {
                minJobs = i;
            }
            servers[minJobs].add(job, currentTime);
        }
    }

    // gets the service time closest to currnent time
    private double getNextEndServiceTime() {
        double nextEndServiceTime = Double.MAX_VALUE;
        for (int i = 0; i < servers.length; i++) {
            if (servers[i].getEndServiceTime() < nextEndServiceTime) {
                nextEndServiceTime = servers[i].getEndServiceTime();
            }

        }
        return nextEndServiceTime;
    }

    // returns the server that completes the next job
    private SingleServerQueue getNextEndServer() {
        // will point to server with the next task to end
        SingleServerQueue nextEndServer = new SingleServerQueue();
        for (int i = 0; i < servers.length; i++) {
            if (servers[i].getEndServiceTime() < nextEndServer.getEndServiceTime()) {
                nextEndServer = servers[i];
            }
        }
        return nextEndServer;
    }

    // simulation loop
    private void doLoop() {
        if (currentTime == nextArrivalTime) {
            addJob(arrivalProcess.nextJob(currentTime), currentTime);
            nextArrivalTime = currentTime + arrivalProcess.nextArrivalTime();
        } else if (currentTime == nextEndServiceTime) {
            completedJobs.add(getNextEndServer().complete(currentTime));
            nextEndServiceTime = getNextEndServiceTime();
        } else if (nextArrivalTime < nextEndServiceTime) {
            currentTime = nextArrivalTime;
        } else {
            currentTime = nextEndServiceTime;
        }
        nextEndServiceTime = getNextEndServiceTime();
    }

}
