package task3.skynet;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Skynet {
    public static void main(String[] args) throws InterruptedException {
        CyclicBarrier dayBarrier = new CyclicBarrier(3);

        Factory factory = new Factory(dayBarrier);
        Faction world = new Faction("World", factory, dayBarrier);
        Faction wednesday = new Faction("Wednesday", factory, dayBarrier);

        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.execute(factory);
        executor.execute(world);
        executor.execute(wednesday);

        Thread.sleep(3000);

        try {
            executor.shutdown();
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("\nРоботов у фракции World: " + world.getRobotsBuilt());
        System.out.println("Роботов у фракции Wednesday: " + wednesday.getRobotsBuilt());

        if (world.getRobotsBuilt() > wednesday.getRobotsBuilt()) {
            System.out.println("\nFaction WORD has strongest army.");
        } else if (world.getRobotsBuilt() < wednesday.getRobotsBuilt()) {
            System.out.println("\nFaction WEDNESDAY has strongest army.");
        } else {
            System.out.println("\nNo one won.");
        }
    }
}
