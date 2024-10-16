import java.util.Random;

public class GetProbabilities {

    Random random = new Random();

    public double[] getStationProb(int numOfStations) {
        double upperBound = 100.0;
        double[] stations = new double[numOfStations];
        for (int i = 0; i < stations.length; i++) {
            if (i == stations.length - 1) {
                stations[i] = upperBound;

            } else {
                stations[i] = random.nextDouble(upperBound);
                upperBound -= stations[i];
            }
        }
        return stations;
    }

}
