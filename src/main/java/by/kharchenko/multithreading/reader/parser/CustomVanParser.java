package by.kharchenko.multithreading.reader.parser;

import by.kharchenko.multithreading.entity.Van;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CustomVanParser {

    public static Van parse(String s) {
        String[] a = s.split(" ");
        return new Van(Boolean.parseBoolean(a[1]), a[0], a[2]);
    }
}
