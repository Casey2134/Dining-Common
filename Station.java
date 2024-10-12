public class Station {

    public class Breakfast {
        Food[] foods;

        public Breakfast(int numOfFood) {
            foods = new Food[numOfFood];
            for (int i = 0; i < numOfFood; i++) {
                foods[i] = new Food(i);
            }
        }

        public Food[] getFoods() {
            return foods;
        }

    }

    public class Lunch {
        Food[] foods;

        public Lunch(int numOfFood) {
            foods = new Food[numOfFood];
            for (int i = 0; i < numOfFood; i++) {
                foods[i] = new Food(i);
            }
        }

        public Food[] getFoods() {
            return foods;
        }

    }

    public class Dinner {
        Food[] foods;

        public Dinner(int numOfFood) {
            foods = new Food[numOfFood];
            for (int i = 0; i < numOfFood; i++) {
                foods[i] = new Food(i);
            }
        }

        public Food[] getFoods() {
            return foods;
        }

    }

    private String stationName;
    Breakfast breakfast;
    Lunch lunch;
    Dinner dinner;

    private SingleServerQueue orderQueue;
    private SingleServerQueue pickupQueue;

    public Station(String newStationName, int numOfBreakfast, int numOfLunch, int numOfDinner) {
        stationName = newStationName;
        orderQueue = new SingleServerQueue();
        pickupQueue = new SingleServerQueue();
        breakfast = new Breakfast(numOfBreakfast);
        lunch = new Lunch(numOfLunch);
        dinner = new Dinner(numOfDinner);

    }

    public Food[] getBreakfast() {
        return breakfast.getFoods();
    }

    public Food[] getLunch() {
        return lunch.getFoods();
    }

    public Food[] getDinner() {
        return dinner.getFoods();
    }

    public double getEndServiceTime() {
        return (pickupQueue.getEndServiceTime());
    }

    public double orderQueueGetEndServiceTime() {
        return (orderQueue.getEndServiceTime());
    }

    public int getOrderQueueLenth() {
        return orderQueue.length();
    }

    public int getPickupQueueLenth() {
        return pickupQueue.length();
    }

    public void addJob(Job job, double currentTime) {
        orderQueue.add(job, currentTime);
    }

    public void transferToPickup(double currentTime) {
        pickupQueue.add(orderQueue.dequeue(currentTime), currentTime);
    }

    public Job completeJob(double currentTime) {
        Job completedJob = pickupQueue.complete(currentTime);
        return completedJob;
    }

}
