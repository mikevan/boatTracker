package com.kmlservice.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class KmlGeneratorService {

    public String generateKml(String inputText) {
        // Parse the input text
        String name = extractName(inputText);
        String description = extractDescription(inputText);
        String timeSpan = extractTimeSpan(inputText);
        String coordinates = extractCoordinates(inputText);

        // Generate KML content
        return String.format(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n" +
            "  <Document>\n" +
            "  <name>Ship Tracker</name>\n" +
            "  <description> A ship tracking script and output used to generate a KML file useful in displaying ship areas</description>\n" +
            "  <Style id=\"thinBlackLine\">\n" +
            "    <LineStyle>\n" +
            "     <color>87000000</color>\n" +
            "     <width>1</width>\n" +
            "    </LineStyle>\n" +
            "  </Style>\n" +
            "  <Style id=\"transparentPolyStyle\">\n" +
            "    <PolyStyle>\n" +
            "      <color>80ADD8E6</color> <!-- Semi-transparent blue -->\n" +
            "      <outline>1</outline>\n" +
            "    </PolyStyle>\n" +
            "  </Style>\n" +
            "  <Placemark>\n" +
            "    <name>%s</name>\n" +
            "    <description>%s</description>\n" +
            "    <styleUrl>#thinBlackLine</styleUrl>\n" +
            "    <styleUrl>#transparentPolyStyle</styleUrl>\n" +
            "    %s\n" +
            "    <Polygon>\n" +
            "      <outerBoundaryIs>\n" +
            "        <LinearRing>\n" +
            "          <coordinates>\n" +
            "            %s\n" +
            "          </coordinates>\n" +
            "        </LinearRing>\n" +
            "      </outerBoundaryIs>\n" +
            "    </Polygon>\n" +
            "  </Placemark>\n" +
            "  </Document>\n" +
            "</kml>", name, description, timeSpan, coordinates
        );
    }

    private String extractName(String inputText) {
        return inputText.split(" MILITARY EXERCISES")[0];
    }

    private String extractDescription(String inputText) {
        String[] parts = inputText.split("ENTERING PROHIBITED\\.");
        return "ENTERING PROHIBITED." + (parts.length > 1 ? parts[1].trim() : "");
    }

    private String extractTimeSpan(String inputText) {
        try {
            String[] parts = inputText.split("FROM ")[1].split(" TO ");
            String startTime = convertToIso(parts[0].trim());
            String endTime = convertToIso(parts[1].split("\\.")[0].trim());

            return String.format(
                "<TimeSpan>\n" +
                "  <begin>%s</begin>\n" +
                "  <end>%s</end>\n" +
                "</TimeSpan>", startTime, endTime
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid time span format in input.");
        }
    }

    private String convertToIso(String time) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddHHmm 'UTC' MMM");
            LocalDateTime dateTime = LocalDateTime.parse(time + " 2024", formatter);
            return dateTime.format(DateTimeFormatter.ISO_DATE_TIME) + "Z";
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid time format: " + time);
        }
    }

    private String extractCoordinates(String inputText) {
        String[] coordinateLines = inputText.split("LINES JOINING")[1]
                                            .split("FROM")[0]
                                            .split("、|AND");
        StringBuilder coordinatesBuilder = new StringBuilder();
        for (String coord : coordinateLines) {
            String[] latLon = coord.trim().split(" ");
            double lat = convertToDecimal(latLon[0]);
            double lon = convertToDecimal(latLon[1]);
            coordinatesBuilder.append(String.format("%.6f,%.6f,0\n", lon, lat));
        }
        return coordinatesBuilder.toString().trim();
    }

    private double convertToDecimal(String coord) {
        String[] parts = coord.split("-");
        double degrees = Double.parseDouble(parts[0]);
        double minutes = Double.parseDouble(parts[1]);
        return degrees + (minutes / 60);
    }
}