public class Main {
    public static void main(String[] args) {
        for(int i = 0 ; i < 5 ; i++) {
            Simulation simulation = new Simulation();
            simulation.run(100, 7); //Monday - Friday
        }
       // simulation.run(12, 9); //Saturday
       // simulation.run(10, 11); //Sunday
    }

}