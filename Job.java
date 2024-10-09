
import java.util.UUID;
import java.util.Random;

public class Job {
    private double creationTime;
    private double serviceTime;
    private double endTime;
    private UUID uuid;

    public Job(double currentTime) {
        creationTime = currentTime;
        uuid = UUID.randomUUID();
    }

    private class Preferences {
        Random random = new Random();
        double upperBound = 100;

        double station1prob = random.nextDouble(upperBound);
        double station2prob;
        double station3prob;
        double station4prob;
        double station5prob;
        double station6prob;
    }

    public void completed(double currentTime) {
        serviceTime = currentTime - creationTime;
        endTime = currentTime;
    }

    public double getCreationTime() {
        return (creationTime);
    }

    public double getServiceTime() {
        return (serviceTime);
    }

    public UUID getUUID() {
        return (uuid);
    }

    public double getEndTime() {
        return (endTime);
    }

    public String toString() {
        return (String.format("UUID = %s | Creation Time = %f, End Time = %f | Service Time = %f", uuid.toString(),
                creationTime, endTime, serviceTime));
    }

    // UNIT TESTS
    public static void unitTest() {
        int successCount = 0;
        int failureCount = 0;

        double startTime = 5.2;
        double completeTime = 7.8;

        Job job = new Job(startTime);

        // If the stored creation time is the same as the startTime input
        if (job.getCreationTime() == 5.2) {
            successCount++;
        } else {
            failureCount++;
        }

        job.completed(completeTime);

        // If the stored end time is the same as the completeTime input
        if (job.getEndTime() == 7.8) {
            successCount++;
        } else {
            failureCount++;
        }

        // If the service time is correct (completeTime - startTime)
        if (job.getServiceTime() == 7.8 - 5.2) {
            successCount++;
        } else {
            failureCount++;
        }

        System.out.println("----------Job Class Unit Test----------\nSuccess Count: " + successCount
                + "\nFailure Count: " + failureCount);
    }
}