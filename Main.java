public class Main {
    public static void main(String[] args) {
        Simulation simulation = new Simulation();
        for(int i = 0 ; i < 5 ; i++) {
            simulation.run(14, 7); //Monday - Friday
        }
        simulation.run(12, 9); //Saturday
        simulation.run(10, 11); //Sunday
    }

}