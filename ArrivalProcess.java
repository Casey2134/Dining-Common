public class ArrivalProcess {
    // the amount of customers coming to the store
    final double arrivalRate = 0.75;

    Exponential exponential = new Exponential();

    public double nextArrivalTime() {
        return exponential.sample();
    }

    public Job nextJob(double currentTime) {
        Job job = new Job(currentTime);
        return job;
    }
}
