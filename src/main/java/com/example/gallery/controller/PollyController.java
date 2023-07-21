package com.example.gallery.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.polly.model.OutputFormat;
import com.example.gallery.service.PollyService;

@RestController
public class PollyController {

    private final PollyService pollyService;

    public PollyController(PollyService pollyService) {
        this.pollyService = pollyService;
    }

    @PostMapping("/synthesize")
    public ResponseEntity<String> getAudio(@RequestParam("text") String text) throws IOException {
        return new ResponseEntity<>(pollyService.synthesize(text.trim(), OutputFormat.Mp3), HttpStatus.OK);
    }

}
