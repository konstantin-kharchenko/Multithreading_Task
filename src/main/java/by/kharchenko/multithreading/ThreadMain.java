package by.kharchenko.multithreading;

import by.kharchenko.multithreading.entity.Van;
import by.kharchenko.multithreading.reader.CustomFileReader;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadMain {
    public static void main(String[] args) throws IOException {
        CustomFileReader fileReader = CustomFileReader.getInstance();
        List<Van> vanList = fileReader.readVan("src\\main\\resources\\file\\vans.txt");
        ExecutorService executor;
        executor = Executors.newFixedThreadPool(vanList.size());
        for (Van van:vanList) {
            executor.execute(van);
        }
        executor.shutdown();
    }
}
