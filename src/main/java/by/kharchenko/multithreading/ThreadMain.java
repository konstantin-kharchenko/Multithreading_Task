package by.kharchenko.multithreading;

import by.kharchenko.multithreading.entity.Van;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadMain {
    public static void main(String[] args) {
        ExecutorService executor;
        executor = Executors.newFixedThreadPool(10);
        executor.execute(new Van(true, "Van 1"));
        executor.execute(new Van(false, "Van 2"));
        executor.execute(new Van(true, "Van 3"));
        executor.execute(new Van(false, "Van 4"));
        executor.execute(new Van(true, "Van 5"));
        executor.execute(new Van(false, "Van 6"));
        executor.execute(new Van(true, "Van 7"));
        executor.execute(new Van(false, "Van 8"));
        executor.execute(new Van(true, "Van 9"));
        executor.execute(new Van(false, "Van 10"));

        executor.shutdown();
    }
}
