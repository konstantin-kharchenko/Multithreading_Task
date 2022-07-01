package by.kharchenko.multithreading.util;

public final class IDGenerator {

    private static int counter;

    private IDGenerator() {
    }

    public static int generate() {
        return ++counter;
    }
}

