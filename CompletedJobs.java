public class CompletedJobs {
    private GenericQueue<Job> completedJobs;

    public CompletedJobs() {
        completedJobs = new GenericQueue<>();
    }

    public void add(Job job) {
        completedJobs.enqueue(job);
    }

    public Job get() {
        return (completedJobs.dequeue());
    }
    public int length(){
        return(completedJobs.length);
    }
}
