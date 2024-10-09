import java.util.Random;
import java.lang.Math;

class Exponential extends RandomDistribution {
    final double lambda = 0.15;
    Random random = new Random();

    double sample() {
        return -(1 / lambda) * Math.log(random.nextDouble(1));
    }

}
