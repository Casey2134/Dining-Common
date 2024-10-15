import java.util.Random;

public class Food {
    int name;
    double cookTime;
    Random random = new Random();

    public Food(int newName) {
        name = newName;
        cookTime = random.nextDouble((float) (1 / 60.0), (float) (1 / 6.0));
    }

    public double getCookTime() {
        return cookTime;
    }

}
