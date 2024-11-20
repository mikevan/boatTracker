package com.kmlservice.controller;

import com.kmlservice.service.KmlGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kml")
public class KmlController {

    @Autowired
    private KmlGeneratorService kmlGeneratorService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateKml(@RequestBody String inputText) {
        String kmlContent = kmlGeneratorService.generateKml(inputText);

        // Return KML response
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/vnd.google-earth.kml+xml");
        headers.add("Content-Disposition", "attachment; filename=\"generatedKML.kml\"");

        return ResponseEntity.ok()
                .headers(headers)
                .body(kmlContent);
    }
}