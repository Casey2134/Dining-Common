// --------------------                                                  --------------------
// -------------------- STILL NEED TO FIND OUT HOW THE FOOD OPTIONS WORK --------------------
// --------------------                                                  --------------------
public class Station {

    public class FoodType {
        int itemCount;
        String name;

        public FoodType(String name, int itemCount) {
            this.itemCount = itemCount;
            this.name = name;
        }

    }

    private String stationName;

    private SingleServerQueue orderQueue;
    private SingleServerQueue pickupQueue;

    private FoodType breakfast;
    private FoodType lunch;
    private FoodType dinner;
    private FoodType[] foodTypes = new FoodType[] {breakfast, lunch, dinner};

    public Station(String stationName, int numOfBreakfast, int numOfLunch, int numOfDinner) {
        this.stationName = stationName;
        this.orderQueue = new SingleServerQueue;
        this.pickupQueue = new SingleServerQueue;

        this.breakfast = new FoodType("breakfast", numOfBreakfast);
        this.lunch = new FoodType("lunch", numOfLunch);
        this.dinner = new FoodType("dinner", numOfDinner);

    }

    public double getEndServiceTime() {
        return (pickupQueue.getEndServiceTime());
    }

    public int getOrderQueueLenth() {
        return orderQueue.length();
    }

    public int getPickupQueueLenth() {
        return pickupQueue.length();
    }

    public void addJob(Job job, double currentTime) {
        orderQueue.add(job);
    }

    public void transferToPickup(double currentTime) {
        pickupQueue.add(orderQueue.dequeue(currentTime));
    }

    public Job completeJob(double currentTime) {
        Job completedJob = pickupQueue.complete(currentTime);
    }



}
