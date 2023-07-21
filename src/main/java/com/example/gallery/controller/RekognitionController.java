package com.example.gallery.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.gallery.service.RekognitionService;

@RestController
public class RekognitionController {

    private final RekognitionService rekognitionService;

    public RekognitionController(RekognitionService rekognitionService) {
        this.rekognitionService = rekognitionService;
    }

    @PostMapping("/recognize")
    public ResponseEntity<String> getAudio(@RequestBody String imageName) {
        return new ResponseEntity<>(rekognitionService.generateImageDescription(imageName), HttpStatus.OK);
    }

}
