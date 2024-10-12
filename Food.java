import java.util.Random;

public class Food {
    int name;
    double cookTime;
    Random random = new Random();

    public Food(int newName) {
        name = newName;
        cookTime = random.nextDouble((1 / 60), (1 / 6));
    }

}
