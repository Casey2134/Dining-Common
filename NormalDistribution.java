import java.util.Random;

class NormalDistribution extends RandomDistribution {
    private double mu = 0.5;
    private double sigma = Math.sqrt((float) 1 / 12);;
    private double xi = 0;
    private Random random = new Random();
    private int x = 30;

    double sample() {
        for (int i = 0; i < x; i++) {
            xi += random.nextDouble(1);
        }
        xi -= x * mu;
        xi /= Math.sqrt((float) x / 12);
        return sigma * xi + mu;
    }
}
