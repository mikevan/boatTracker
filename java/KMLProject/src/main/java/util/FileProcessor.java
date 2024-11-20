package util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileProcessor {
    public List<File> processInputFiles(String[] args) {
        List<File> inputFiles = new ArrayList<>();

        for (String arg : args) {
            File file = new File(arg);

            if (file.isDirectory()) {
                // Get all .txt files in the directory
                File[] files = file.listFiles((dir, name) -> name.endsWith(".txt"));
                if (files != null) {
                    for (File f : files) {
                        inputFiles.add(f);
                    }
                }
            } else if (file.isFile()) {
                inputFiles.add(file);
            } else {
                System.out.println("Skipping invalid argument: " + arg);
            }
        }
        return inputFiles;
    }
}