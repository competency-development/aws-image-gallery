package com.example.gallery.service;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.model.OutputFormat;
import com.amazonaws.services.polly.model.SynthesizeSpeechRequest;
import com.amazonaws.services.polly.model.SynthesizeSpeechResult;
import com.amazonaws.services.polly.model.VoiceId;

@Service
public class PollyService {

    private final AmazonPolly amazonPolly;

    private final S3AudioBucketService audiosBucketService;

    public PollyService(AmazonPolly amazonPolly, S3AudioBucketService audiosBucketService) {
        this.amazonPolly = amazonPolly;
        this.audiosBucketService = audiosBucketService;
    }

    public String synthesize(String text, OutputFormat format) throws IOException {
        SynthesizeSpeechRequest synthReq = new SynthesizeSpeechRequest().withText(text).withVoiceId(VoiceId.Raveena)
                .withOutputFormat(format);
        SynthesizeSpeechResult synthRes = amazonPolly.synthesizeSpeech(synthReq);

        InputStream audioStream = synthRes.getAudioStream();

        String audioName = RandomStringUtils.randomAlphanumeric(24) + ".mp3";

        return audiosBucketService.upload(audioName, audioStream);
    }
}
