package util;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class KMLContent {
    private StringBuilder kml;

    public KMLContent() {
        kml = new StringBuilder();
    }

    public void startKMLDocument(String name, String description) {
        kml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
           .append("<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n")
           .append("  <Document>\n")
           .append("    <name>").append(name).append("</name>\n")
           .append("    <description>").append(description).append("</description>\n")
           .append("    <Style id=\"thinBlackLine\">\n")
           .append("      <LineStyle>\n")
           .append("        <color>87000000</color>\n")
           .append("        <width>1</width>\n")
           .append("      </LineStyle>\n")
           .append("    </Style>\n")
           .append("    <Style id=\"transparentPolyStyle\">\n")
           .append("      <PolyStyle>\n")
           .append("        <color>80ADD8E6</color>\n")
           .append("        <outline>1</outline>\n")
           .append("      </PolyStyle>\n")
           .append("    </Style>\n");
    }

    public void addFileToKML(File file) throws IOException {
        String content = Files.readString(file.toPath());

        // Extract data using regex
        String placemarkName = extractRegex(content, "^(.*?)(?=\\s+IN\\s+AREA\\s+BOUNDED)");
        if (placemarkName == null) placemarkName = "Military Exercise Area";

        String description = extractRegex(content, "(?:UTC NOV\\.\\s+)(.*?)(?=\\s*$)");
        if (description == null) description = "No description provided.";

        String fromTime = extractRegex(content, "FROM\\s+(\\d{2}\\d{4} UTC \\w{3})");
        String toTime = extractRegex(content, "TO\\s+(\\d{2}\\d{4} UTC \\w{3})");
        String fromISO = ISO8601Converter.convertToISO8601(fromTime);
        String toISO = ISO8601Converter.convertToISO8601(toTime);

        List<String> coordinates = extractCoordinates(content);

        if (coordinates.size() != 4) {
            throw new IllegalArgumentException("Expected 4 coordinates in file '" + file.getName() +
                                               "', but found " + coordinates.size());
        }

        // Append to KML
        kml.append("    <Placemark>\n")
           .append("      <name>").append(placemarkName).append("</name>\n")
           .append("      <description>").append(description).append("</description>\n")
           .append("      <styleUrl>#thinBlackLine</styleUrl>\n")
           .append("      <Polygon>\n")
           .append("        <outerBoundaryIs>\n")
           .append("          <LinearRing>\n")
           .append("            <coordinates>\n");
        for (String coord : coordinates) {
            kml.append("              ").append(coord).append("\n");
        }
        kml.append("            </coordinates>\n")
           .append("          </LinearRing>\n")
           .append("        </outerBoundaryIs>\n")
           .append("      </Polygon>\n")
           .append("    </Placemark>\n");
    }

    public void finishKMLDocument(String outputPath) throws IOException {
        kml.append("  </Document>\n</kml>\n");
        Files.writeString(Path.of(outputPath), kml.toString());
    }

    private String extractRegex(String content, String pattern) {
        Matcher matcher = Pattern.compile(pattern, Pattern.MULTILINE).matcher(content);
        return matcher.find() ? matcher.group(1) : null;
    }

    private List<String> extractCoordinates(String content) {
        List<String> coordinates = new ArrayList<>();
        Matcher matcher = Pattern.compile("(\\d+)-(\\d+\\.\\d+)([NS])\\s+(\\d+)-(\\d+\\.\\d+)([EW])").matcher(content);
        while (matcher.find()) {
            double lat = Integer.parseInt(matcher.group(1)) + Double.parseDouble(matcher.group(2)) / 60;
            if (matcher.group(3).equals("S")) lat = -lat;

            double lon = Integer.parseInt(matcher.group(4)) + Double.parseDouble(matcher.group(5)) / 60;
            if (matcher.group(6).equals("W")) lon = -lon;

            coordinates.add(String.format("%.6f,%.6f,0", lon, lat));
        }
        return coordinates;
    }
}