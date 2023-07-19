package com.example.gallery.controller;

import com.amazonaws.services.polly.model.OutputFormat;
import com.example.gallery.service.PollyService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class PollyController {

    private final PollyService pollyService;

    public PollyController(PollyService pollyService) {
        this.pollyService = pollyService;
    }

    @PostMapping("/synthesize")
    public byte[] getAudio(@RequestBody String text) throws IOException {
        return pollyService.synthesize(text, OutputFormat.Mp3);
    }
}
