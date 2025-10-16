package task3.skynet;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.Random;

public class Factory implements Runnable {
    private final List<PartType> dailyProduction = new ArrayList<>();
    private final List<PartType> availableParts = new ArrayList<>();
    private final CyclicBarrier dayBarrier;
    private final Random random = new Random();
    private int day = 0;

    public Factory(CyclicBarrier dayBarrier) {
        this.dayBarrier = dayBarrier;
    }

    private void produceParts() {
        int partsQuantity = random.nextInt(11); //0-10 деталей
        for (int i = 0; i < partsQuantity; i++) {
            PartType[] parts = PartType.values();
            PartType randomPart = parts[random.nextInt(parts.length)];
            dailyProduction.add(randomPart);
        }
    }

    public synchronized List<PartType> takeParts(int maxParts) {
        List<PartType> takenParts = new ArrayList<>();
        int partsToTake = Math.min(maxParts, availableParts.size());
        for (int i = 0; i < partsToTake; i++) {
            takenParts.add(availableParts.removeFirst());
        }
        return takenParts;
    }

    @Override
    public void run() {
        try {
            while (day < 10) {
                //Днем производим детали
                produceParts();
                System.out.println("\nДень " + (day + 1) + ": фабрика произвела " + dailyProduction.size() + " деталей");

                availableParts.addAll(dailyProduction); //Выкладываем детали для фракций
                dailyProduction.clear();
                dayBarrier.await(); //Разрешаем фракциям забирать детали
                dayBarrier.await(); //Ночь: ждем пока фракции заберут детали
                day++;
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }
}
