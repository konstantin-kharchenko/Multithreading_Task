package by.kharchenko.multithreading.reader;

import by.kharchenko.multithreading.entity.Van;
import by.kharchenko.multithreading.reader.parser.CustomVanParser;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CustomFileReader {
    private static final Logger logger = LogManager.getLogger(CustomFileReader.class);

    private static CustomFileReader instance;

    private CustomFileReader() {
    }

    public static CustomFileReader getInstance() {
        if (instance == null) {
            instance = new CustomFileReader();
        }
        return instance;
    }

    public List<Van> readVan(String path) throws IOException {
        Path pathClass = Paths.get(path);
        List<Van> vans = new ArrayList<>();
        List<String> lines = Files.lines(pathClass).toList();
        for (var s : lines) {
            vans.add(CustomVanParser.parse(s));
        }
        logger.log(Level.INFO, "DATA WAS READ FROM A FILE");
        return vans;
    }
}
