package task3.skynet;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Faction implements Runnable {
    private final String name;
    private final Factory factory;
    private final List<PartType> storage = new ArrayList<>();
    private final AtomicInteger robotsBuilt = new AtomicInteger(0);
    private final CyclicBarrier dayBarrier;
    private int day = 0;

    public Faction(String name, Factory factory, CyclicBarrier dayBarrier) {
        this.name = name;
        this.factory = factory;
        this.dayBarrier = dayBarrier;
    }

    private void collectParts() {
        List<PartType> newParts = factory.takeParts(5);
        storage.addAll(newParts);
        System.out.println(name + " забрал " + newParts.size() + " деталей. Всего деталей на складе: " + storage.size());
    }

    private void assembleRobots() {
        int heads = 0, torsos = 0, hands = 0, feet = 0;

        for (PartType part : storage) { //Количество доступных деталей у фракции
            switch (part) {
                case HEAD -> heads++;
                case TORSO -> torsos++;
                case HAND -> hands++;
                case FOOT -> feet++;
            }
        }

        int possibleRobots = Math.min(Math.min(heads, torsos), Math.min(hands / 2, feet / 2));
        if (possibleRobots > 0) {
            removePartsFromStorage(possibleRobots);
            robotsBuilt.addAndGet(possibleRobots);
            System.out.println(name + " собрал роботов: " + possibleRobots + ". Всего роботов: " + robotsBuilt);
        }
    }

    private void removePartsFromStorage(int robotsQuantity) {
        int headsToRemove = robotsQuantity;
        int torsosToRemove = robotsQuantity;
        int handsToRemove = robotsQuantity * 2;
        int feetToRemove = robotsQuantity * 2;

        Iterator<PartType> iterator = storage.iterator();
        while (iterator.hasNext() && (headsToRemove > 0 || torsosToRemove > 0 ||handsToRemove > 0 || feetToRemove > 0)) {
            PartType part = iterator.next();
            switch (part) {
                case HEAD -> {
                    if (headsToRemove > 0) {
                        iterator.remove();
                        headsToRemove--;
                    }
                }
                case TORSO -> {
                    if (torsosToRemove > 0) {
                        iterator.remove();
                        torsosToRemove--;
                    }
                }
                case HAND -> {
                    if (handsToRemove > 0) {
                        iterator.remove();
                        handsToRemove--;
                    }
                }
                case FOOT -> {
                    if (feetToRemove > 0) {
                        iterator.remove();
                        feetToRemove--;
                    }
                }
            }
        }
    }

    public int getRobotsBuilt() {
        return robotsBuilt.get();
    }

    public List<PartType> getStorage() {
        return storage;
    }

    @Override
    public void run() {
        try {
            dayBarrier.await(); //Ждем пока фабрика производит детали
            while (day < 10) {
                collectParts(); //Ночь: забираем детали
                assembleRobots(); //Собираем роботов из накопленных деталей
                dayBarrier.await();
                dayBarrier.await();//День (фабрика производит детали)
                day++;
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }
}
