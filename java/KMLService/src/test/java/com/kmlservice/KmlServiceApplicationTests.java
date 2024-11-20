package com.kmlservice;

import com.kmlservice.service.KmlGeneratorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class KmlServiceApplicationTests {

    @Autowired
    private KmlGeneratorService kmlGeneratorService;

    @Test
    void contextLoads() {
    }

    @Test
    void testGeneratedKmlIsValidXml() throws Exception {
        // Input text for the service
        String inputText = "37.422, -122.084";

        // Generate KML content
        String kmlContent = kmlGeneratorService.generateKml(inputText);

        // Validate that the response is a valid XML
        assertDoesNotThrow(() -> parseXml(kmlContent), "Generated KML should be valid XML");

        // Parse the XML to a DOM Document
        Document document = parseXml(kmlContent);

        // Assert root element is <kml>
        assertEquals("kml", document.getDocumentElement().getNodeName(), "Root element should be <kml>");

        // Assert it contains a <Document> element
        assertNotNull(document.getElementsByTagName("Document").item(0), "KML should contain a <Document> element");

        // Assert it contains a <Placemark> element
        assertNotNull(document.getElementsByTagName("Placemark").item(0), "KML should contain a <Placemark> element");

        // Assert it contains a <coordinates> element with correct content
        String coordinates = document.getElementsByTagName("coordinates").item(0).getTextContent().trim();
        assertEquals("-122.084,37.422,0", coordinates, "Coordinates should match the input");
    }

    private Document parseXml(String xmlContent) throws ParserConfigurationException, SAXException, Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        ByteArrayInputStream input = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
        return builder.parse(input);
    }
}