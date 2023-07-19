package com.example.gallery.service;

import java.io.IOException;

import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.model.*;
import org.springframework.stereotype.Service;

@Service
public class PollyService {

    private final AmazonPolly amazonPolly;

    public PollyService(AmazonPolly amazonPolly) {
        this.amazonPolly = amazonPolly;
    }

    public byte[] synthesize(String text, OutputFormat format) throws IOException {
        SynthesizeSpeechRequest synthReq = new SynthesizeSpeechRequest().withText(text).withVoiceId(VoiceId.Raveena)
                .withOutputFormat(OutputFormat.Mp3);
        SynthesizeSpeechResult synthRes = amazonPolly.synthesizeSpeech(synthReq);
        return synthRes.getAudioStream().readAllBytes();
    }
}
