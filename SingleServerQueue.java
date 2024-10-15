public class SingleServerQueue {
    private GenericQueue<Job> queue;
    private Job jobInService;
    private double nextEndServiceTime;
    private NormalDistribution serviceTimeDistribution;

    public SingleServerQueue() {
        queue = new GenericQueue<>();
        jobInService = null;
        nextEndServiceTime = Double.MAX_VALUE;
        serviceTimeDistribution = new NormalDistribution();
    }

    public int length() {
        return (queue.length);
    }

    public void add(Job job, double currentTime) {
        if (jobInService == null) {
            jobInService = job;
            nextEndServiceTime = currentTime + serviceTimeDistribution.sample();
        } else {
            queue.enqueue(job);
        }
    }

    public double getEndServiceTime() {
        return (nextEndServiceTime);
    }

    public Job complete(double currentTime) {
        Job tempJob = dequeue(currentTime);
        if (tempJob != null) {
            tempJob.completed(currentTime);
            return tempJob;
        } else
            System.out.println("Null job inputed");
        return null;
    }

    public Job dequeue(double currentTime) {
        Job tempJob = jobInService;
        if (queue.isEmpty()) {
            jobInService = null;
            nextEndServiceTime = Double.MAX_VALUE;
        } else {
            jobInService = queue.dequeue();
            nextEndServiceTime = currentTime + serviceTimeDistribution.sample();
        }
        return (tempJob);
    }

    public static void doUnitTests() {
        int[] tests = new int[] { 0, 0 }; // Succeeded, Failed
        SingleServerQueue testQueue = new SingleServerQueue();
        Job[] testJobs = new Job[] { new Job(0), new Job(30), new Job(56), new Job(103),
                new Job(293) };
        // Add Tests
        for (int i = 0; i < testJobs.length; i++) {
            testQueue.add(testJobs[i], testJobs[i].getCreationTime() + 30.4);
            tests[0]++;
        }
        // Complete Tests
        for (int j = 0; j < testJobs.length; j++) {
            // Next End Service Time Tests
            if (testQueue.nextEndServiceTime == testJobs[j].getCreationTime() || testQueue.nextEndServiceTime < 0) {
                tests[1]++;
            }
            tests[0]++;
            testQueue.complete(testJobs[j].getCreationTime() + 30.4);
            tests[0]++;
        }
        System.out.println("Fail count: " + tests[1]);
        System.out.println("Success count: " + tests[0]);
    }
}
