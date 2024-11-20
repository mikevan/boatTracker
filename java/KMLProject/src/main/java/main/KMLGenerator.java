package main;

import java.io.File;
import java.io.IOException;
import java.util.List;
import util.FileProcessor;
import util.KMLContent;
import util.ISO8601Converter;

public class KMLGenerator {
    public static void main(String[] args) {
        try {
            // Process input arguments for files or directories
            FileProcessor fileProcessor = new FileProcessor();
            List<File> inputFiles = fileProcessor.processInputFiles(args);

            if (inputFiles.isEmpty()) {
                throw new IOException("No valid files found to process.");
            }

            // Initialize KML content
            KMLContent kmlContent = new KMLContent();
            kmlContent.startKMLDocument("Ship Tracker", 
                "A ship tracking script and output used to generate a KML file useful in displaying ship areas");

            // Process each file and add data to KML content
            for (File file : inputFiles) {
                kmlContent.addFileToKML(file);
            }

            // Finish and write KML
            kmlContent.finishKMLDocument("combined_military_exercise_areas.kml");
            System.out.println("Combined KML file 'combined_military_exercise_areas.kml' created successfully.");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}