public class Person extends Job {
    enum type {
        VISITOR, STUDENT
    };

    public Person(double currentTime, type personType) {
        super(currentTime);
    }

    boolean dairyFree;
    boolean glutenFree;
    boolean vegitarian;
    boolean vegan;
    double satisfaction = 100;

}
